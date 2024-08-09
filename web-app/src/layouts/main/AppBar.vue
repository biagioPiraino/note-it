<template>
  <v-app-bar density="comfortable" flat border col>
    <v-container class="d-flex align-center">
      <v-btn
        icon=""
        color="primary"
        variant="plain"
        @click="router.push({path: 'profile'})">
        <v-avatar
          color="grey"
          size="32">
          <v-img cover :src="user!.picture"/>
        </v-avatar>
      </v-btn>

      <v-menu v-if="$vuetify.display.xs">
        <template v-slot:activator="{ props }">
          <v-btn color="primary" icon="mdi-menu" v-bind="props"/>
        </template>

        <v-list>
          <v-list-item
            v-for="(link, i) in links"
            :key="i">
            <v-list-item-title>
              <v-btn
                :key="i"
                :text="link"
                @click="router.push({path: link})"
                color="primary"
                variant="plain"
                class="text-uppercase"
              ></v-btn>
            </v-list-item-title>
          </v-list-item>
        </v-list>
      </v-menu>

      <v-btn
        v-else
        v-for="(link, i) in links"
        :key="i"
        :text="link"
        @click="router.push({path: link})"
        color="primary"
        variant="plain"
        class="text-uppercase"
      ></v-btn>
      <v-spacer/>
      <v-btn
        @click="logoutUser"
        variant="plain"
        color="primary"
        class="me-7"
        append-icon="mdi-logout">
        Logout
      </v-btn>
    </v-container>
  </v-app-bar>
</template>

<script setup lang="ts">
import router from "@/router";
import {useAuth0} from "@auth0/auth0-vue"

const links: string[] = [
  "dashboard",
  "notes",
  "tasks"
]

const {logout, user} = useAuth0();

function logoutUser() {
  logout({logoutParams: {returnTo: window.location.origin}})
}
</script>
