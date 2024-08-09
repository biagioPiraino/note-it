import {CategoryType} from "../../models/enums/CategoryType";
import {CategoryData} from "../../models/CategoryData";
import {ApiClient} from "./ApiClient";

export class CategoriesClient extends ApiClient {

  private readonly _endpointPrefix: string = import.meta.env.VITE_API_CATEGORIES;

  async getCategories(): Promise<Array<CategoryData>> {
    const headers: Headers = await super.createBaseHeaders();
    const request: Request = new Request(this._endpointPrefix, {
      method: "GET",
      headers: headers
    });

    return await this.fetchCategories(request);
  }

  async getCategoriesByType(type: CategoryType): Promise<Array<CategoryData>> {
    const headers: Headers = await super.createBaseHeaders();
    const endpoint: string = this._endpointPrefix + "/type" + `/${type}`;
    const request: Request = new Request(endpoint, {
      method: "GET",
      headers: headers
    });

    return await this.fetchCategories(request);
  }

  async createCategory(category: CategoryData): Promise<Boolean> {
    const headers: Headers = await super.createBaseHeaders();
    const request: Request = new Request(this._endpointPrefix, {
      method: "POST",
      headers: headers,
      body: JSON.stringify(category)
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

  async updateCategory(category: CategoryData): Promise<Boolean> {
    const headers: Headers = await super.createBaseHeaders();
    const endpoint: string = this._endpointPrefix + `/${category.id}`;
    const request: Request = new Request(endpoint, {
      method: "PUT",
      headers: headers,
      body: JSON.stringify(category)
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

  async deleteCategory(categoryId: string): Promise<Boolean> {
    const headers: Headers = await super.createBaseHeaders();
    const endpoint: string = this._endpointPrefix + `/${categoryId}`;
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

  private async fetchCategories(request: Request): Promise<Array<CategoryData>> {
    try {
      const response: Response = await fetch(request);
      if (!response.ok) {
        super.throwClientException(`An error occurred while querying the API. Status:${response.status}`);
      }
      return await response.json();
    } catch (error) {
      console.error("Error:", error);
      return new Array<CategoryData>;
    }
  }
}
