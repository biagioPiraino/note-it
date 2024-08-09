import {TaskData} from "../../models/TaskData";
import {ApiClient} from "./ApiClient";

export class TasksClient extends ApiClient {

  private readonly _endpointPrefix: string = import.meta.env.VITE_API_TASKS;

  async getTasks(): Promise<Array<TaskData>> {
    const headers: Headers = await super.createBaseHeaders();
    const request: Request = new Request(this._endpointPrefix, {
      method: "GET",
      headers: headers
    });

    try {
      const response: Response = await fetch(request);
      if (!response.ok) {
        super.throwClientException(`An error occurred while querying the API. Status:${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error("Error:", error);
      return new Array<TaskData>;
    }
  }

  async createTask(task: TaskData): Promise<Boolean> {
    const headers: Headers = await super.createBaseHeaders();
    const request: Request = new Request(this._endpointPrefix, {
      method: "POST",
      headers: headers,
      body: JSON.stringify(task)
    });

    try {
      const response: Response = await fetch(request);
      if (!response.ok) {
        super.throwClientException(`An error occurred while querying the API. Status:${response.status}`);
      }
      return true
    } catch (error) {
      console.error("Error:", error);
      return false;
    }
  }

  async updateTask(task: TaskData): Promise<Boolean> {
    const headers: Headers = await super.createBaseHeaders();
    const endpoint: string = this._endpointPrefix + `/${task.id}`;
    const request: Request = new Request(endpoint, {
      method: "PUT",
      headers: headers,
      body: JSON.stringify(task)
    });

    try {
      const response: Response = await fetch(request);
      if (!response.ok) {
        super.throwClientException(`An error occurred while querying the API. Status:${response.status}`);
      }
      return true
    } catch (error) {
      console.error("Error:", error);
      return false;
    }
  }

  async deleteTask(taskId: string): Promise<Boolean> {
    const headers: Headers = await super.createBaseHeaders();
    const endpoint: string = this._endpointPrefix + `/${taskId}`;
    const request: Request = new Request(endpoint, {
      method: "DELETE",
      headers: headers
    });

    try {
      const response: Response = await fetch(request);
      if (!response.ok) {
        super.throwClientException(`An error occurred while querying the API. Status:${response.status}`);
      }
      return true;
    } catch (error) {
      console.error(error);
      return false;
    }
  }
}
