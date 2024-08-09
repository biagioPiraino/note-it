<template>
  <v-container>
    <v-row no-gutters>
      <v-col cols="12" sm="11">
        <v-sheet
          rounded="lg">
          <v-form @submit.prevent>
            <v-container class="pa-6">
              <v-row dense>
                <v-col cols="8">
                  <v-text-field
                    :rules="editorValidator.titleRules"
                    v-model="note.title"
                    color="primary"
                    label="Title ✍️"
                    variant="outlined"/>
                </v-col>

                <v-col cols="4">
                  <v-select
                    :items="existingCategories"
                    :item-props="selectCategoryProps"
                    v-model="note.category"
                    :rules="editorValidator.categoryRules"
                    color="primary"
                    label="Category"
                    variant="outlined">

                    <template v-slot:selection="{ item }">
                      <v-chip
                        v-if="item.title !== ''"
                        density="compact"
                        :text="item.title"
                        :color="item.value.colour"/>
                      <span v-else class="text-grey">Select</span>
                    </template>

                    <template v-slot:item="{ props, item }">
                      <v-list-item v-bind="props">
                        <template v-slot:append>
                          <v-icon
                            :color="item.value.colour"
                            icon="mdi-palette-outline"/>
                        </template>
                      </v-list-item>
                    </template>

                    <template v-slot:no-data>
                      <v-list-item>
                        <div class="text-body-2">No categories available 😪</div>
                        <template v-slot:append>
                          <v-btn
                            class="ms-2"
                            variant="text"
                            density="compact"
                            color="teal-lighten-1"
                            icon="mdi-shape-plus"
                            @click="isOverlayActive = !isOverlayActive"/>
                        </template>
                      </v-list-item>
                    </template>
                  </v-select>

                  <categories-overlay
                    :is-overlay-active="isOverlayActive"
                    :context="CategoryType.Note"
                    :is-editing="true"
                    @closing-overlay="handleClosingOverlay"/>

                </v-col>

                <v-col cols="12">
                  <v-textarea
                    :rules="editorValidator.summaryRules"
                    v-model="note.summary"
                    class="mt-2"
                    color="primary"
                    label="Summary 🗒️"
                    variant="outlined"
                    rows="3"
                    auto-grow/>
                </v-col>
                <v-col cols="12">
                  <v-tabs
                    class="mb-2"
                    v-model="tab"
                    align-tabs="start"
                    color="primary"
                    fixed-tabs>
                    <v-tab
                      v-for="tab in editorTabs"
                      :key="tab"
                      :value="tab">
                      {{ tab }}
                    </v-tab>
                  </v-tabs>

                  <v-window
                    v-model="tab">
                    <v-window-item
                      v-for="tab in editorTabs"
                      :key="tab"
                      :value="tab">

                      <v-textarea
                        :rules="editorValidator.noteContentRules"
                        v-model="note.content"
                        v-if="tab === 'editor'"
                        class="mt-3"
                        color="primary"
                        variant="outlined"
                        rows="15"
                        auto-grow/>

                      <v-container v-else>
                        <rendered-note :note-content="note.content"/>
                      </v-container>
                    </v-window-item>
                  </v-window>
                </v-col>
              </v-row>
            </v-container>
          </v-form>
        </v-sheet>
      </v-col>
      <v-col cols="12" sm="1" class="d-flex flex-row flex-sm-column justify-end justify-sm-start">
        <v-btn
          flat
          class="my-3 my-sm-1 mx-sm-3"
          color="primary"
          icon="mdi-arrow-left"
          @click="router.push({path: 'notes'})"/>
        <v-btn
          flat
          class="my-3 my-sm-1 mx-sm-3"
          color="primary"
          @click="saveNote"
          icon="mdi-content-save-check-outline"/>
        <v-btn
          flat
          class="my-3 my-sm-1 mx-sm-3"
          color="primary"
          icon="mdi-shape-outline"
          @click="isOverlayActive = !isOverlayActive"/>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import router from "@/router";
import {onBeforeMount, ref} from "vue";
import {NoteData} from "../../models/NoteData";
import RenderedNote from "@/components/RenderedNote.vue";
import {EditorValidator} from "../../models/validators/EditorValidator";
import {EditingNoteStore} from "@/store/EditingNote";
import {CategoryData} from "../../models/CategoryData";
import CategoriesOverlay from "@/components/CategoriesOverlay.vue";
import {CategoryType} from "../../models/enums/CategoryType";
import {CategoriesClient} from "../../api/clients/CategoriesClient";
import {NotesClient} from "../../api/clients/NotesClient";
import {GlobalSnackbar} from "@/store/Snackbar";
import {useAuth0} from "@auth0/auth0-vue";

const tab = ref<null | string>(null);
const editorTabs: string[] = [
  "editor",
  "preview"
]

const snackbar = GlobalSnackbar();
const {getAccessTokenSilently} = useAuth0();
const notesClient = ref<NotesClient>(new NotesClient(getAccessTokenSilently));
const categoriesClient = ref<CategoriesClient>(new CategoriesClient(getAccessTokenSilently));

const note = ref<NoteData>(new NoteData());
const isOverlayActive = ref<boolean>(false);
const editorValidator = ref<EditorValidator>(new EditorValidator());

const editingNoteStore = ref(EditingNoteStore());
const existingCategories = ref<Array<CategoryData>>(new Array<CategoryData>());

onBeforeMount(async () => {
  await loadCategories();
  if (editingNoteStore.value.IsEditingNote) {
    note.value = editingNoteStore.value.GetEditingNote;
  }
})

function selectCategoryProps(category: CategoryData) {
  return {
    title: category.name,
    value: category
  }
}

async function loadCategories() {
  existingCategories.value = await categoriesClient.value.getCategoriesByType(CategoryType.Note)
}

async function handleClosingOverlay() {
  isOverlayActive.value = false;
  await loadCategories();
}

async function saveNote() {
  const isCreation: Boolean = note.value.id == "";
  let success: Boolean;

  if (isCreation) {
    success = await notesClient.value.createNote(note.value);
  } else {
    success = await notesClient.value.updateNote(note.value);
  }

  if (success) {
    snackbar.show("mdi-hand-okay", "success", "Note correctly created.");
    await router.push({path: 'notes'})
  } else {
    snackbar.show("mdi-close-box-outline", "error","An error occurred.");
  }
}

</script>
