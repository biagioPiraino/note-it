<template>
  <v-main class="bg-grey-lighten-3">
    <global-snackbar/>
    <router-view />
  </v-main>
</template>

<script setup lang="ts">
import GlobalSnackbar from "@/components/GlobalSnackbar.vue";
</script>
