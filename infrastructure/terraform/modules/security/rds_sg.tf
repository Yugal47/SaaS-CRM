resource "aws_security_group" "rds_sg" {
name	= "rds-sg"
vpc	= ""

ingress {
  from_port	= 5432
  to_prot	= 5432
  protocol	= "tcp"
  cidr_blocks	= ["10.0.0.0/16"]
}
egress {
  from_port	= 0
  to_port	= 0
  protocol	= "-1"
  cidr_blocks	= ["0.0.0.0/0"]
  }
}
resource "aws_db_index" "postgres" {
  
