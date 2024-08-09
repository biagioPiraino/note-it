<template>
  <v-container class="fill-height">
    <v-responsive class="align-center text-center fill-height">
      <v-row class="d-flex align-center justify-center" no-gutters>
        <v-col cols="12" xs="12">
          <div class="text-caption py-3">Please login to access</div>
          <v-btn color="primary" size="large" @click="login" variant="elevated">
            <v-icon icon="mdi-login" size="medium" start/>
            Log in
          </v-btn>
        </v-col>
      </v-row>
    </v-responsive>
  </v-container>
</template>

<script setup lang="ts">
import {useAuth0} from "@auth0/auth0-vue"

const {loginWithRedirect} = useAuth0();

function login() {
  loginWithRedirect();
}
</script>
