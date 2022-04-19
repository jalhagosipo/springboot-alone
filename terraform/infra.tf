resource "aws_key_pair" "alone_ec2" {
  key_name   = "alone_ec2"
  public_key = file("~/.ssh/id_rsa.pub")
}

## 보안 그룹
resource "aws_security_group" "alone_web" {
  name        = "Alone EC2 Security Group"
  description = "Alone EC2 Security Group"
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