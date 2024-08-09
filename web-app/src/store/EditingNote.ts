import {defineStore} from "pinia";
import {NoteData} from "../../models/NoteData";

export const EditingNoteStore = defineStore('editingNoteStore', {
  state: () => {
    return {
      editingNote: null as NoteData | null
    }
  },
  getters: {
    IsEditingNote(state): boolean {
      return state.editingNote !== null;
    },
    GetEditingNote(state): NoteData {
      return state.editingNote as NoteData;
    }
  },
  actions: {
    UpdateStore(editingNote: NoteData): void {
      this.editingNote = editingNote;
    },
    CleanStore(): void {
      this.editingNote = null;
    }
  }
});
