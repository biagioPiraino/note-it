<template>
  <v-container>
    <v-row no-gutters>
      <v-col cols="12" sm="11">
        <v-sheet
          rounded="lg">
          <v-data-table-server
            v-model="expandedNotes"
            :key="tableState"
            :items-length="itemLength"
            :headers="tableHeaders"
            :loading="isTableLoading"
            :items="tableItems"
            :search="searchString"
            loading-text="Loading notes..."
            no-data-text="No notes available 😪"
            class="pb-2"
            @update:options="loadTableData"
            @update:items-per-page="loadTableData"
            show-expand>

            <template v-slot:top>
              <v-container class="d-flex align-center">

                <v-text-field
                  bg-color="primary_light"
                  rounded
                  v-model="searchString"
                  density="compact"
                  label="Search"
                  prepend-inner-icon="mdi-magnify"
                  variant="solo-filled"
                  flat
                  hide-details
                  single-line
                  clearable/>

                <v-spacer/>
                <v-spacer/>
                <v-spacer/>
                <v-spacer/>

              </v-container>
            </template>

            <template v-slot:[`item.category`]="{item}">
              <v-chip
                size="small"
                :text="item.category.name"
                :color="item.category.colour"/>
            </template>

            <template v-slot:[`item.modifiedAt`]="{item}">
              {{
                new Date((item as NoteData).modifiedAt).toLocaleDateString("en-UK", {
                  weekday: 'long',
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric',
                  hour: 'numeric',
                  minute: 'numeric'
                })
              }}
            </template>

            <template v-slot:expanded-row="{ columns, item }">
              <tr>
                <td :colspan="columns.length">
                  <v-container class="py-9 px-5">
                    <div class="table-rendered-note">
                      <rendered-note :note-content="item.content"/>
                    </div>
                    <v-container class="pa-0 ma-0 pt-4 d-flex">
                      <v-spacer/>
                      <v-btn
                        flat
                        class="me-2"
                        size="small"
                        color="error"
                        text="delete"
                        prepend-icon="mdi-delete"
                        @click="deleteNote(item)"/>
                      <v-btn
                        flat
                        text="edit"
                        size="small"
                        color="primary"
                        prepend-icon="mdi-pen"
                        @click="() => navigateToEditor(item)"/>
                    </v-container>
                  </v-container>
                </td>
              </tr>
            </template>

          </v-data-table-server>
        </v-sheet>
      </v-col>
      <v-col cols="12" sm="1" class="d-flex flex-row flex-sm-column justify-end justify-sm-start">

        <v-btn
          flat
          class="my-3 my-sm-2 mx-3"
          color="primary"
          icon="mdi-pen-plus"
          @click="router.push({path: 'editor'})"/>

        <v-btn
          flat
          class="my-3 my-sm-1 mx-sm-3"
          color="primary"
          icon="mdi-shape-outline"
          @click="isOverlayActive = !isOverlayActive"/>

        <categories-overlay
          :is-overlay-active="isOverlayActive"
          :context="CategoryType.Note"
          :is-editing="false"
          @closing-overlay="handleClosingOverlay"/>

      </v-col>
    </v-row>
  </v-container>
</template>

<style scoped>

.table-rendered-note {
  max-height: 400px;
  overflow: auto;
  scrollbar-color: #4BB #E0F2F1;
  scrollbar-width: thin;
}

</style>

<script lang="ts" setup>
import {onBeforeMount, ref} from "vue";
import router from "@/router";
import {NoteData} from "../../models/NoteData";
import RenderedNote from "@/components/RenderedNote.vue";
import {EditingNoteStore} from "@/store/EditingNote";
import CategoriesOverlay from "@/components/CategoriesOverlay.vue";
import {CategoryType} from "../../models/enums/CategoryType";
import {GlobalSnackbar} from "@/store/Snackbar";
import {NotesClient} from "../../api/clients/NotesClient";
import {useAuth0} from "@auth0/auth0-vue";

const snackbar = GlobalSnackbar();
const {getAccessTokenSilently} = useAuth0();
const notesClient = ref<NotesClient>(new NotesClient(getAccessTokenSilently));

const tableState = ref<number>(0);
const itemLength = ref<number>(0);
const searchString = ref<string>("");
const isTableLoading = ref<boolean>(false);

const isOverlayActive = ref<boolean>(false);
const editingNoteStore = ref(EditingNoteStore());

const tableItems = ref<Array<NoteData>>(new Array<NoteData>());
const expandedNotes = ref<Array<NoteData>>(new Array<NoteData>());

const tableHeaders: Array<any> = [
  {title: 'Title', key: 'title', align: 'start', sortable: true},
  {title: 'Summary', key: 'summary', align: 'start', sortable: false},
  {title: 'Category', key: 'category', align: 'start', sortable: false},
  {title: 'Modified at', key: 'modifiedAt', align: 'start', sortable: true},
];

onBeforeMount(() => {
  editingNoteStore.value.CleanStore();
})

function navigateToEditor(selectedNote: NoteData) {
  editingNoteStore.value.UpdateStore(selectedNote);
  router.push({path: '/editor'})
}

async function loadTableData({page, itemsPerPage, sortBy, search}: any) {
  isTableLoading.value = true;

  const notes = (await notesClient.value.getNotes()).filter(item => {
    return !(search && !item.title.toLowerCase().includes(search.toLowerCase()));
  });

  itemLength.value = notes.length;

  const start = (page - 1) * itemsPerPage;
  const end = start + itemsPerPage;

  if (sortBy.length) {
    const sortKey = sortBy[0].key;
    const sortOrder = sortBy[0].order;

    notes.sort((a: any, b: any) => {
      const aValue = a[sortKey];
      const bValue = b[sortKey];

      if (typeof aValue === 'number' && typeof bValue === 'number') {
        return sortOrder === 'desc' ? bValue - aValue : aValue - bValue;
      } else if (typeof aValue === 'string' && typeof bValue === 'string') {
        return sortOrder === 'desc'
          ? bValue.localeCompare(aValue)
          : aValue.localeCompare(bValue);
      } else {
        return 0;
      }
    })
  }

  tableItems.value = notes.slice(start, end);
  isTableLoading.value = false;
}

async function deleteNote(note: NoteData) {
  const success: Boolean = await notesClient.value.deleteNote(note.id);
  if (success) {
    snackbar.show("mdi-hand-okay", "success", "Note correctly deleted.");
    reloadTable();
  } else {
    snackbar.show("mdi-close-box-outline", "error", "An error occurred.");
  }
}

function handleClosingOverlay() {
  isOverlayActive.value = false;
}

function reloadTable() {
  tableState.value += 1;
}

</script>
