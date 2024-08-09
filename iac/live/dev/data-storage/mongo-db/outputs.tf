output "project_id" {
  value       = module.mongo_project.project_id
  description = "Project created ID"
}

output "project_name" {
  value       = module.mongo_project.project_name
  description = "Project created name"
}