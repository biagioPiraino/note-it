<template>
  <v-container>
    <v-row>
      <v-col cols="12" sm="8">
        <v-sheet rounded="lg">
          <v-list rounded="lg">
            <v-list-item
              title="Latest Notes">
              <template v-slot:prepend>
                <v-avatar color="primary">
                  <v-icon color="white">mdi-note</v-icon>
                </v-avatar>
              </template>
            </v-list-item>

            <v-divider class="my-2"/>

            <v-container
              class="d-flex justify-center"
              v-if="isLoadingNotes">
              <v-progress-circular
                color="primary"
                indeterminate/>
            </v-container>

            <v-list-item
              v-for="(note, i) in latestNotes"
              :key="i"
              link
              @click="navigateToEditor(note)">
              <template v-slot:title>
                  <p class="font-weight-bold">{{ note.title }}</p>
              </template>
              <template v-slot:subtitle>
                <p class="font-weight-light">Last modified at
                  {{
                    new Date(note.modifiedAt).toLocaleDateString("en-UK", {
                      weekday: 'long',
                      year: 'numeric',
                      month: 'long',
                      day: 'numeric',
                      hour: 'numeric',
                      minute: 'numeric'
                    })
                  }}</p>
              </template>
              <template v-slot:append>
                <v-chip
                  size="small"
                  :text="note.category.name"
                  :color="note.category.colour"/>
              </template>
            </v-list-item>
          </v-list>
        </v-sheet>
      </v-col>

      <v-col cols="12" sm="4">
        <v-sheet rounded="lg">
          <v-list rounded="lg">
            <v-list-item
              title="Upcoming Tasks">
              <template v-slot:prepend>
                <v-avatar color="primary">
                  <v-icon color="white">mdi-reminder</v-icon>
                </v-avatar>
              </template>
            </v-list-item>

            <v-divider class="my-2"/>

            <v-container
              class="d-flex justify-center"
              v-if="isLoadingTasks">
              <v-progress-circular
                color="primary"
                indeterminate/>
            </v-container>

            <v-list-item
              v-for="(task, i) in upcomingTasks"
              :key="i">
              <template v-slot:title>
                {{
                  `${task.title} - ${new Date(task.scheduledAt).toLocaleDateString()}`
                }}
              </template>
              <template v-slot:append>
                <v-chip
                  size="small"
                  :text="task.category.name"
                  :color="task.category.colour"/>
              </template>
            </v-list-item>
          </v-list>
        </v-sheet>
      </v-col>
    </v-row>
  </v-container>
</template>

<script lang="ts" setup>
import {NotesClient} from "../../api/clients/NotesClient";
import {onBeforeMount, ref} from "vue";
import {NoteData} from "../../models/NoteData";
import {TasksClient} from "../../api/clients/TasksClient";
import {TaskData} from "../../models/TaskData";
import router from "@/router";
import {EditingNoteStore} from "@/store/EditingNote";
import {useAuth0} from "@auth0/auth0-vue";

const {getAccessTokenSilently} = useAuth0();

const notesClient = new NotesClient(getAccessTokenSilently);
const tasksClient = new TasksClient(getAccessTokenSilently);

const isLoadingNotes = ref<Boolean>(true);
const isLoadingTasks = ref<Boolean>(true);

const editingNoteStore = ref(EditingNoteStore());

const latestNotes = ref<Array<NoteData>>(new Array<NoteData>());
const upcomingTasks = ref<Array<TaskData>>(new Array<TaskData>());

onBeforeMount(async () => {
  await loadNotes();
  isLoadingNotes.value = false;
  await loadTasks();
  isLoadingTasks.value = false;
})

async function loadNotes() {
  const notes = await notesClient.getNotes();
  const sorted = notes.sort((a, b) => {
    return new Date(b.modifiedAt).getTime() - new Date(a.modifiedAt).getTime()
  });
  latestNotes.value = sorted.slice(-10);
}

async function loadTasks() {
  const tasks = await tasksClient.getTasks();
  const sorted = tasks.sort((a, b) => {
    return new Date(a.scheduledAt).getTime() - new Date(b.scheduledAt).getTime()
  });
  upcomingTasks.value = sorted.slice(-5);
}

function navigateToEditor(selectedNote: NoteData) {
  editingNoteStore.value.UpdateStore(selectedNote);
  router.push({path: '/editor'})
}
</script>
