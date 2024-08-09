variable "project_id" {
  description = "The cluster associated project ID"
  type        = string
}

variable "cluster_name" {
  description = "The cluster name"
  type        = string
}

variable "cluster_type" {
  description = "The cluster type"
  type        = string
}

variable "backup_enabled" {
  description = "Flag to signal whether or not to enable cluster backups"
  type        = bool
}

variable "tags" {
  description = "Cluster associated tags"
  type        = map(string)
}

variable "replications_configs" {
  description = "Configuration related to regional deployment of clusters and replication sets"

  type = list(object({
    # Only applies to SHARDED and GEOSHARDED clusters
    num_shards = number

    regions_config = list(object({
      priority      = number
      provider_name = string
      region_name   = string

      backing_provider_name = string

      # Free tier instance size is of type "M0"
      instance_size = string
      node_count    = number
    }))
  }))
}