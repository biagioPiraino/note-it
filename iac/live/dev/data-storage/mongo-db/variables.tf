#====================#
# Provider variables #
#====================#

variable "mongodbatlas_public_key" {
  description = "Atlas public key for programmatic access"
  type        = string
  sensitive   = true
}

variable "mongodbatlas_private_key" {
  description = "Atlas private key for programmatic access"
  type        = string
  sensitive   = true
}

#===================#
# Project variables #
#===================#

variable "project_name" {
  description = "The project name"
  type        = string
  default     = "go-development"
}

variable "access_list_ips" {
  description = "A list of ips to define the access list of the project"
  type        = map(string)

  default = {
    "home" = ""
  }
}

#============================#
# Advanced cluster variables #
#============================#

variable "tags" {
  description = "Custom tags to apply to cluster"
  type        = map(string)

  default = {
    "environment" = "development"
  }
}

variable "cluster_name" {
  description = "The cluster name"
  type        = string
  default     = "go-development"
}

variable "cluster_type" {
  description = "The cluster type"
  type        = string
  default     = "REPLICASET"
}

variable "backup_enabled" {
  description = "Flag to signal whether or not to enable cluster backups"
  type        = bool
  default     = false
}

variable "replications_configs" {
  description = "Configuration related to regional deployment of clusters and replication sets"

  type = list(object({
    num_shards = number

    regions_config = list(object({
      priority      = number
      provider_name = string
      region_name   = string

      backing_provider_name = string

      instance_size = string
      node_count    = number
    }))
  }))

  default = [
    {
      num_shards = 1

      regions_config = [
        {
          priority      = 7
          provider_name = "TENANT"
          region_name   = "US_EAST_1"
          instance_size = "M0"

          backing_provider_name = "AWS"

          node_count = 1
        }
      ]
    }
  ]
}

#==========================#
# Database users variables #
#==========================#

# Admin user variables
variable "admin_username" {
  description = "The admin username"
  type        = string
  sensitive   = true
}

variable "admin_password" {
  description = "The admin password"
  type        = string
  sensitive   = true
}

variable "admin_roles" {
  description = "The admin roles"

  type = list(object({
    role_name     = string
    database_name = string
  }))

  default = [
    {
      role_name     = "atlasAdmin"
      database_name = "admin"
    }
  ]
}

variable "admin_labels" {
  description = "The admin labels"
  type        = map(string)

  default = {
    "role" = "cluster admin"
  }
}

variable "admin_scopes" {
  description = "The admin scopes"

  type = list(object({
    name = string
    type = string
  }))

  default = [
    {
      # Dependent on the cluster name defined above
      name = "go-development"
      type = "CLUSTER"
    }
  ]
}

# Application user variables
variable "application_username" {
  description = "The application user username"
  type        = string
  sensitive   = true
}

variable "application_password" {
  description = "The application user password"
  type        = string
  sensitive   = true
}

variable "application_roles" {
  description = "The application user roles"

  type = list(object({
    role_name     = string
    database_name = string
  }))

  default = [
    {
      role_name     = "readWrite"
      database_name = "note-it"
    }
  ]
}

variable "application_labels" {
  description = "The application user labels"
  type        = map(string)

  default = {
    "role" = "application user"
  }
}

variable "application_scopes" {
  description = "The application user scopes"

  type = list(object({
    name = string
    type = string
  }))

  default = [
    {
      # Dependent on the cluster name defined above
      name = "go-development"
      type = "CLUSTER"
    }
  ]
}

# Datagrip user variables
variable "datagrip_username" {
  description = "The datagrip user username"
  type        = string
  sensitive   = true
}

variable "datagrip_password" {
  description = "The datagrip user password"
  type        = string
  sensitive   = true
}

variable "datagrip_roles" {
  description = "The datagrip user roles"

  type = list(object({
    role_name     = string
    database_name = string
  }))

  default = [
    {
      role_name     = "dbAdmin"
      database_name = "admin"
    }
  ]
}

variable "datagrip_labels" {
  description = "The datagrip user labels"
  type        = map(string)

  default = {
    "role" = "database admin"
  }
}

variable "datagrip_scopes" {
  description = "The datagrip user scopes"

  type = list(object({
    name = string
    type = string
  }))

  default = [
    {
      # Dependent on the cluster name defined above
      name = "go-development"
      type = "CLUSTER"
    }
  ]
}
