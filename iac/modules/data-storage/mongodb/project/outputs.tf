output "project_id" {
  value = mongodbatlas_project.project.id
  description = "Project created ID"
}

output "project_name" {
  value = mongodbatlas_project.project.name
  description = "Project created name"
}