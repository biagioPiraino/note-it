import {ApiClient} from "./ApiClient";
import {UserData} from "../../models/UserData";

export class UsersClient extends ApiClient {

  private readonly _endpointPrefix: string = import.meta.env.VITE_API_USERS;

  async updateUser(user: UserData): Promise<Boolean> {
    const headers: Headers = await super.createBaseHeaders();
    const endpoint: string = this._endpointPrefix + `/${user.id}`;
    const request: Request = new Request(endpoint, {
      method: "PUT",
      headers: headers,
      body: JSON.stringify(user)
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

  async resetPassword(email: string | undefined): Promise<Boolean> {
    const data: any = {
      client_id: import.meta.env.VITE_AUTH_CLIENT,
      email: email,
      connection: 'Username-Password-Authentication'
    };

    const endpoint: string = import.meta.env.VITE_AUTH_IDENTIFIER + "/dbconnections/change_password";
    const request: Request = new Request(endpoint, {
      method: "POST",
      headers: {'Content-Type': 'application/json'},
      body: JSON.stringify(data)
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
}
