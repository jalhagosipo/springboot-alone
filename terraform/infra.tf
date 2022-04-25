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
}

# EIP
resource "aws_eip" "elasticip" {
  instance = aws_instance.web.id
}

output "EIP" {
  value = aws_eip.elasticip.public_ip
}