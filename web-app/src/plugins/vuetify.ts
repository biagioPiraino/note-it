import '@mdi/font/css/materialdesignicons.css'
import 'vuetify/styles'

import { createVuetify } from 'vuetify'

export default createVuetify({
  theme: {
    themes: {
      light: {
        colors: {
          primary: '#4BB',
          primary_light: '#E0F2F1',
          secondary: '#8DD',
          error: '#F55',
          success: '#8D8',
          warning: '#FA5',
          info: '#8AD'
        },
      },
    },
  },
})
