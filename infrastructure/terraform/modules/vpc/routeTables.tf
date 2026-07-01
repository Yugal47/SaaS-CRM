resource "aws_route_table" "public_rt" {
  vpc_id = aws_vpc.main.id
  route {
    cidr_block = "0.0.0.0/0"
    gateway_id = aws_internet_gateway.igw.id
  }
  tags = {
    Name = "public_rt"
  }
}

resource "aws_route_table_association" "public" {
  count			= length(aws_subnet.public)
  subnet_id		= aws_subnet.public[count.index].id
  route_table_id	= aws_route_table.public_rt.id
}

resource "aws_route_table" "private_rt" {
  count		= length(aws_nat_gateway.ngw)
  vpc_id	= aws_vpc.main.id
  route {
    cidr_block	= "0.0.0.0/0"
    nat_gateway_id = aws_nat_gateway.ngw[count.index].id
  }
  tags = { Name	= "private_rt" }
}
resource "aws_route_table_association" "private" {
  count			= length(aws_subnet.private)
  subnet_id		= aws_subnet.private[count.index].id
  route_table_id	= aws_route_table.private_rt[count.index % length(aws_route_table.private)]
} 
