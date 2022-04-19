provider "aws" {
  shared_credentials_files = ["~/.aws/credentials"]
  region = "ap-northeast-2"
}

data "http" "myip" {
  url = "http://ipv4.icanhazip.com"
}
