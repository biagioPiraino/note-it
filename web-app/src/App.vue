<template>
  <loading v-if="isLoading"/>
  <v-container v-else fluid>
    <router-view v-if="isAuthenticated"/>
    <login v-else/>
  </v-container>
</template>

<script lang="ts" setup>
import {useAuth0} from "@auth0/auth0-vue";
import Login from "@/components/Login.vue";
import Loading from "@/components/Loading.vue";

const {isAuthenticated, isLoading} = useAuth0();
</script>
