terraform {
  required_version = ">= 1.0.0, < 2.0.0"

  required_providers {
    mongodbatlas = {
      source  = "mongodb/mongodbatlas"
      version = "~> 1.15.0"
    }
  }
}

resource "mongodbatlas_database_user" "user" {
  username           = var.username
  password           = var.password
  project_id         = var.project_id
  auth_database_name = var.auth_database_name

  dynamic "roles" {
    for_each = var.roles

    content {
      role_name     = roles.value.role_name
      database_name = roles.value.database_name
    }
  }

  dynamic "scopes" {
    for_each = var.scopes

    content {
      name = scopes.value.name
      type = scopes.value.type
    }
  }

  dynamic "labels" {
    for_each = var.labels

    content {
      key   = labels.key
      value = labels.value
    }
  }
}