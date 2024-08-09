terraform {
  required_version = ">= 1.0.0, < 2.0.0"

  required_providers {
    mongodbatlas = {
      source  = "mongodb/mongodbatlas"
      version = "~> 1.15.0"
    }
  }
}

resource "mongodbatlas_project" "project" {
  name   = var.project_name
  org_id = data.mongodbatlas_roles_org_id.organization.org_id
}

resource "mongodbatlas_project_ip_access_list" "access_list" {
  project_id = mongodbatlas_project.project.id
  for_each   = var.access_list_ips
  ip_address = each.value
  comment    = each.key
}

data "mongodbatlas_roles_org_id" "organization" {}