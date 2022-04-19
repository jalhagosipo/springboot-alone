resource "aws_key_pair" "alone_ec2" {
  key_name   = "alone_ec2"
  public_key = file("~/.ssh/id_rsa.pub")
}