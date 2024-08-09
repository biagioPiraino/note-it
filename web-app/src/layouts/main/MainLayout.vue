<template>
  <v-app>
    <app-bar/>
    <selected-view/>
    <app-footer/>
  </v-app>
</template>

<script setup lang="ts">
  import AppBar from "./AppBar.vue"
  import SelectedView from './View.vue'
  import AppFooter from './AppFooter.vue'
</script>
