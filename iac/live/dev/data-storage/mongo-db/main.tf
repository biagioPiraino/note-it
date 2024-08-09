terraform {
  required_version = ">= 1.0.0, < 2.0.0"

  required_providers {
    mongodbatlas = {
      source  = "mongodb/mongodbatlas"
      version = "~> 1.15.0"
    }
  }
}

provider "mongodbatlas" {
  private_key = var.mongodbatlas_private_key
  public_key  = var.mongodbatlas_public_key
}

module "mongo_project" {
  source          = "../../../../modules/data-storage/mongodb/project"
  project_name    = var.project_name
  access_list_ips = var.access_list_ips
}

module "advanced_cluster" {
  source = "../../../../modules/data-storage/mongodb/advanced-cluster"

  project_id     = module.mongo_project.project_id
  cluster_name   = var.cluster_name
  cluster_type   = var.cluster_type
  backup_enabled = var.backup_enabled
  tags           = var.tags

  replications_configs = var.replications_configs

  depends_on = [module.mongo_project]
}

module "admin_user" {
  source = "../../../../modules/data-storage/mongodb/database-user"

  username   = var.admin_username
  password   = var.admin_password
  project_id = module.mongo_project.project_id

  roles  = var.admin_roles
  labels = var.admin_labels
  scopes = var.admin_scopes

  depends_on = [module.mongo_project, module.advanced_cluster]
}

module "application_user" {
  source = "../../../../modules/data-storage/mongodb/database-user"

  username   = var.application_username
  password   = var.application_password
  project_id = module.mongo_project.project_id

  roles  = var.application_roles
  labels = var.application_labels
  scopes = var.application_scopes

  depends_on = [module.mongo_project, module.advanced_cluster]
}

module "datagrip_user" {
  source = "../../../../modules/data-storage/mongodb/database-user"

  username   = var.datagrip_username
  password   = var.datagrip_password
  project_id = module.mongo_project.project_id

  roles  = var.datagrip_roles
  labels = var.datagrip_labels
  scopes = var.datagrip_scopes

  depends_on = [module.mongo_project, module.advanced_cluster]
}
