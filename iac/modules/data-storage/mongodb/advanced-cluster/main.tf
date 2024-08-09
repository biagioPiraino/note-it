terraform {
  required_version = ">= 1.0.0, < 2.0.0"

  required_providers {
    mongodbatlas = {
      source  = "mongodb/mongodbatlas"
      version = "~> 1.15.0"
    }
  }
}

resource "mongodbatlas_advanced_cluster" "cluster" {
  project_id     = var.project_id
  name           = var.cluster_name
  cluster_type   = var.cluster_type
  backup_enabled = var.backup_enabled

  dynamic "replication_specs" {
    for_each = var.replications_configs

    content {
      num_shards = replication_specs.value.num_shards

      dynamic "region_configs" {
        for_each = replication_specs.value.regions_config

        content {
          priority      = region_configs.value.priority
          provider_name = region_configs.value.provider_name
          region_name   = region_configs.value.region_name

          backing_provider_name = region_configs.value.backing_provider_name

          electable_specs {
            instance_size = region_configs.value.instance_size
            node_count    = region_configs.value.node_count
          }
        }
      }
    }
  }

  dynamic "tags" {
    for_each = var.tags

    content {
      key   = tags.key
      value = tags.value
    }
  }
}