import vuetify from './vuetify'
import pinia from '../store'
import router from '../router'

import type { App } from 'vue'
import {createAuth0} from "@auth0/auth0-vue";

export function registerPlugins (app: App) {
  app
    .use(vuetify)
    .use(router)
    .use(pinia)
    .use(createAuth0({
      domain: import.meta.env.VITE_AUTH_DOMAIN,
      clientId: import.meta.env.VITE_AUTH_CLIENT,
      authorizationParams: {
        redirect_uri: window.location.origin,
        audience: import.meta.env.VITE_API_IDENTIFIER
      }
    }))
}
