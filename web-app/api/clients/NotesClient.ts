import {NoteData} from "../../models/NoteData";
import {ApiClient} from "./ApiClient";

export class NotesClient extends ApiClient {

  private readonly _endpointPrefix: string = import.meta.env.VITE_API_NOTES;

  async getNotes(): Promise<Array<NoteData>> {
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
      return new Array<NoteData>;
    }
  }

  async createNote(note: NoteData): Promise<Boolean> {
    const headers: Headers = await super.createBaseHeaders();
    const request: Request = new Request(this._endpointPrefix, {
      method: "POST",
      headers: headers,
      body: JSON.stringify(note)
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

  async updateNote(note: NoteData): Promise<Boolean> {
    const headers: Headers = await super.createBaseHeaders();
    const endpoint: string = this._endpointPrefix + `/${note.id}`;
    const request: Request = new Request(endpoint, {
      method: "PUT",
      headers: headers,
      body: JSON.stringify(note)
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

  async deleteNote(noteId: string): Promise<Boolean> {
    const headers: Headers = await super.createBaseHeaders();
    const endpoint: string = this._endpointPrefix + `/${noteId}`;
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
