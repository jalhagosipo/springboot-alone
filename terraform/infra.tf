resource "aws_key_pair" "alone_ec2" {
  key_name   = "alone_ec2"
  public_key = file("~/.ssh/id_rsa.pub")
}

## 보안 그룹
resource "aws_security_group" "alone_web" {
  name        = "Alone EC2 Security Group"
  description = "Alone EC2 Security Group"

  # Terraform removes the default rule
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 22                                           #인바운드 시작 포트
    to_port = 22                                             #인바운드 끝나는 포트
    protocol = "tcp"                                         #사용할 프로토콜
    cidr_blocks = ["${chomp(data.http.myip.body)}/32"]       #허용할 IP 범위
  }
  ingress {
    from_port = 8080
    to_port = 8080
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
  ingress {
    from_port = 443
    to_port = 443
    protocol = "tcp"
    cidr_blocks = ["0.0.0.0/0"]
  }
}

resource "aws_security_group" "alone_RDS" {
  name        = "Alone RDS Security Group"
  description = "Alone RDS Security Group"

  # Terraform removes the default rule
  egress {
    from_port = 0
    to_port = 0
    protocol = "-1"
    cidr_blocks = ["0.0.0.0/0"]
  }

  ingress {
    from_port = 3306
    to_port = 3306
    protocol = "tcp"
    # 내 IP와 ec2의 보안그룹 id를 rds 보안 그룹의 인바운드로 추가
    # ec2와 rds간에 접근이 가능함 -> ec2의 경우 이후에 2,3대가 될 수 있는데
    # 매번 IP를 등록할 수는 없으니 보편적으로 이렇게 보안 그룹간에 연동을 진행
    cidr_blocks = ["${chomp(data.http.myip.body)}/32"]
    security_groups = [aws_security_group.alone_web.id]
  }
}

# EC2
resource "aws_instance" "web" {
  ami = "ami-02de72c5dc79358c9" # Amazon Linux 2 Kernel 5.10 AMI 2.0.20220406.1 x86_64 HVM gp2
  instance_type = "t2.micro"
  key_name = aws_key_pair.alone_ec2.key_name
  vpc_security_group_ids = [
    aws_security_group.alone_web.id
  ]
  tags = {
    Name                = "springboot-webservice"
  }
  root_block_device {
    volume_size         = 30 # 프리티어 30GB까지 가능. default 8GB
  }
  connection {
    type     = "ssh"
    user     = "ec2-user"
    public_key = file("~/.ssh/id_rsa")
    host     = aws_instance.web.public_ip
  }
}

# EIP
resource "aws_eip" "elasticip" {
  instance = aws_instance.web.id
}

output "EIP" {
  value = aws_eip.elasticip.public_ip
}

# RDS
resource "aws_db_instance" "alone_db" {
  allocated_storage = 20
  engine = "mariadb"
  engine_version = "10.6"
  instance_class = "db.t2.micro"
  username = "" # TODO
  password = "" # TODO
  parameter_group_name = aws_db_parameter_group.alone_db_parameter_group.name
  publicly_accessible = true
  skip_final_snapshot = true
  vpc_security_group_ids = [
    aws_security_group.alone_RDS.id
  ]
  identifier = "alone-db"
  db_name = "alone_db"
  apply_immediately=true
}

resource "aws_db_parameter_group" "alone_db_parameter_group" {
  name   = "springboot-webservice"
  family = "mariadb10.6"

  parameter {
    name  = "character_set_connection"
    value = "utf8mb4"
  }

  parameter {
    name  = "character_set_client"
    value = "utf8mb4"
  }

  parameter {
    name  = "character_set_server"
    value = "utf8mb4"
  }

  parameter {
    name  = "character_set_database"
    value = "utf8mb4"
  }

  parameter {
    name  = "max_connections"
    value = 150
  }
}