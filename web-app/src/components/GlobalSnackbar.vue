<template>
  <v-snackbar
    location="bottom left"
    variant="elevated"
    v-model="snackbar.isVisible"
    :color="snackbar.color"
    :timeout="2000">
    <v-icon class="mb-1 me-1" :icon="snackbar.icon"/>
    {{ snackbar.message }}
  </v-snackbar>
</template>

<script setup lang="ts">
import {GlobalSnackbar} from "@/store/Snackbar";
import {ref} from "vue";

const snackbar = ref(GlobalSnackbar());
</script>
