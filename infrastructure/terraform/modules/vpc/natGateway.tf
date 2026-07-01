resource "aws_eip" "eip" {
  count = length(aws_subnet.public)
  domain = "vpc"
  tags = {
    Name = "ElasticIP-${count.index + 1}"
  }
}

resource "aws_nat_gateway" "ngw" {
  count		= length(aws_subnet.public)

  allocation_id = aws_eip.eip[count.index].id
  subnet_id	= aws_subnet.public[count.index].id
  depends_on	= [aws_internet_gateway.igw]

  tags = {
    Name = "nat-${count.index + 1}"
  }
}
