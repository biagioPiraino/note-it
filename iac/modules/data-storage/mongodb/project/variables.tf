variable "project_name" {
  description = "The project name"
  type        = string
}

variable "access_list_ips" {
  description = "A list of ips to define the access list of the project"
  type        = map(string)
}