variable "username" {
  description = "The user's username"
  type        = string
  sensitive   = true
}

variable "password" {
  description = "The user's password"
  type        = string
  sensitive   = true
}

variable "project_id" {
  description = "The associated project ID"
  type        = string
}

variable "auth_database_name" {
  description = "Database against which Atlas authenticate the user"
  type        = string
  default     = "admin"
}

variable "roles" {
  description = "The user's roles associated to a database"

  type = list(object({
    role_name     = string
    database_name = string
  }))
}

variable "scopes" {
  description = "The clusters or data lakes the user has access to"

  type = list(object({
    name = string
    type = string
  }))

  validation {
    condition = alltrue([
      for v in var.scopes :
      contains(["CLUSTER", "DATA_LAKE"], v.type)
    ])
    error_message = "Bad request. The supported scope types are CLUSTER and DATA_LAKE."
  }
}

variable "labels" {
  description = "Labels used to categorise the user"
  type        = map(string)
}