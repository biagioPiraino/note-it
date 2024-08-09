<template>
  <v-overlay
    @after-leave="() => emitClosingSignal()"
    class="align-center justify-center"
    :model-value="isOverlayActive">

    <v-sheet rounded="lg" width="550px" height="450px">
      <v-container class="d-flex flex-row align-center pb-2 px-5">
        <div class="text-h6 text-teal-lighten-1 me-1">Categories</div>
        <v-btn
          variant="text"
          density="compact"
          color="teal-lighten-1"
          icon="mdi-shape-plus"
          @click="() => addCategory()"/>
      </v-container>

      <v-container v-if="categories.length > 0" class="pa-0 ma-0 categories-container">
        <v-container
          v-for="(item, i) in categories"
          :key="i"
          :value="item"
          class="d-flex flex-column ma-0 pa-0">

          <v-text-field
            v-model="item.name"
            class="px-6 py-1"
            density="compact"
            variant="outlined"
            color="primary"
            label="Name"
            single-line
            rounded
            hide-details>

            <template v-slot:append-inner>
              <v-btn
                :color="item.colour"
                variant="flat"
                density="compact"
                icon="mdi-palette-outline">

                <v-overlay
                  activator="parent"
                  location-strategy="connected"
                  class="align-center justify-end">

                  <v-color-picker
                    v-model="item.colour"
                    class="pa-2"
                    swatches-max-height="350px"
                    hide-inputs
                    hide-canvas
                    show-swatches/>

                </v-overlay>

              </v-btn>
            </template>

            <template v-slot:append>
              <v-container
                class="ma-0 pa-0">
                <v-row class="ga-2 justify-center" no-gutters>
                  <v-btn
                    v-if='item.id !== ""'
                    @click="() => deleteCategory(item.id)"
                    flat
                    size="small"
                    color="error"
                    icon="mdi-delete-forever-outline"/>
                  <v-btn
                    @click="() => saveCategory(item)"
                    flat
                    size="small"
                    color="primary"
                    icon="mdi-content-save-check-outline"/>
                </v-row>
              </v-container>
            </template>

          </v-text-field>

        </v-container>
      </v-container>

      <v-container v-else class="px-5 py-1">
        <div class="text-body-2 text-center">No categories available 😪</div>
      </v-container>

    </v-sheet>

  </v-overlay>
</template>

<style scoped>

.categories-container {
  max-height: 80%;
  overflow: auto;
  scrollbar-color: #4BB #E0F2F1;
  scrollbar-width: thin;
}

</style>

<script setup lang="ts">
import {onBeforeMount, ref} from "vue";
import {CategoryType} from "../../models/enums/CategoryType";
import {CategoriesClient} from "../../api/clients/CategoriesClient";
import {CategoryData} from "../../models/CategoryData";
import {GlobalSnackbar} from "@/store/Snackbar";
import {useAuth0} from "@auth0/auth0-vue";

const props = defineProps<{
  isOverlayActive: boolean;
  isEditing: boolean;
  context: CategoryType;
}>();

const emit = defineEmits<{
  closingOverlay: [void]
}>()

const snackbar = GlobalSnackbar();
const {getAccessTokenSilently} = useAuth0();

const categories = ref<Array<CategoryData>>(new Array<CategoryData>());
const client = ref<CategoriesClient>(new CategoriesClient(getAccessTokenSilently));

onBeforeMount(async () => {
  await loadCategories();
});

function addCategory() {
  categories.value.push(new CategoryData(props.context));
}

async function deleteCategory(id: string): Promise<void> {
  const success = await client.value.deleteCategory(id);
  if (success) {
    snackbar.show("mdi-hand-okay", "success", "Category correctly deleted.");
    await loadCategories();
  } else {
    snackbar.show("mdi-close-box-outline", "error", "An error occurred.");
  }
}

async function saveCategory(category: CategoryData): Promise<void> {
  let success: Boolean;
  const isCreation = category.id === "";

  if (isCreation) success = await client.value.createCategory(category);
  else success = await client.value.updateCategory(category);

  if (success) {
    const message = isCreation ? "Category correctly created." : "Category correctly updated.";
    snackbar.show("mdi-hand-okay", "success", message);
    await loadCategories();
  } else {
    snackbar.show("mdi-close-box-outline", "error", "An error occurred.");
  }
}

async function loadCategories() {
  categories.value = await client.value.getCategoriesByType(props.context)
}

function emitClosingSignal() {
  emit('closingOverlay')
}

</script>
