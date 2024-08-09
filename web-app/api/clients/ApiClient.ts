export class ApiClient {
  private readonly getAuthToken: () => Promise<string>;

  constructor(getAuthToken: () => Promise<string>) {
    this.getAuthToken = getAuthToken;
  }

  async createBaseHeaders(): Promise<Headers> {
    const token: string = await this.getAuthToken();
    const headers: Headers = new Headers();

    headers.append("Content-Type", "application/json");
    headers.append("Authorization", 'Bearer ' + token);

    return headers;
  }

  throwClientException(message: string): void {
    throw new Error(message);
  }
}
