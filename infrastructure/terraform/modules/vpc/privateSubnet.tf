resource "aws_subnet" "private" {
  count		= length(var.private_subnet_cidrs)

  vpc_id	= aws_vpc.main.id
  availability_zone = var.availability_zones[count.index % length(var.availability_zones)]
  cidr_block	= var.private_subnet_cidrs[count.index]

  tags = {
    Name = "private-${count.index + 1}"
  }
}
