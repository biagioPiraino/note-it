import {defineStore} from 'pinia'

interface Snackbar {
  isVisible: boolean;
  icon: string;
  color: string;
  message: string;
}

export const GlobalSnackbar = defineStore('snackbar', {
  state: (): Snackbar => {
    return {
      isVisible: false,
      icon: "",
      color: "",
      message: ""
    }
  },
  getters: {},
  actions: {
    show(icon: string, color: string, message: string) {
      this.icon = icon;
      this.color = color;
      this.message = message;
      this.isVisible = true;
    }
  }
});
