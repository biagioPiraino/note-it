<template>
  <v-container>
    <v-row no-gutters>
      <v-col cols="12" sm="11">
        <v-sheet
          rounded="lg">
          <v-data-table-server
            v-model="expandedTasks"
            :key="tableState"
            :items-length="itemLength"
            :headers="tableHeaders"
            :loading="isTableLoading"
            :items="tableItems"
            :search="searchString"
            loading-text="Loading tasks..."
            no-data-text="No tasks available 😪"
            class="pb-2"
            items-per-page="10"
            @update:options="loadTableData"
            @update:items-per-page="loadTableData">

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

            <template v-slot:[`item.status`]="{item}">
              <v-chip
                size="small"
                :text="statusMapper.getDescription(item.status)"
                :color="statusMapper.getColour(item.status)"/>
            </template>

            <template v-slot:[`item.category`]="{item}">
              <v-chip
                size="small"
                :text="item.category.name"
                :color="item.category.colour"/>
            </template>

            <template v-slot:[`item.modifiedAt`]="{item}">
              {{
                new Date((item as TaskData).modifiedAt).toLocaleDateString("en-UK", {
                  weekday: 'long',
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric',
                  hour: 'numeric',
                  minute: 'numeric'
                })
              }}
            </template>

            <template v-slot:[`item.scheduledAt`]="{item}">
              {{
                new Date((item as TaskData).scheduledAt).toLocaleDateString("en-UK", {
                  weekday: 'long',
                  year: 'numeric',
                  month: 'long',
                  day: 'numeric'
                })
              }}
            </template>

            <template v-slot:[`item.actions`]="{item}">
              <v-item-group class="d-flex ga-2 pt-3 pb-3">
                <v-btn
                  border
                  color="error"
                  icon="mdi-delete-circle-outline"
                  height="35"
                  width="35"
                  variant="text"
                  @click="deleteTask(item.id)"/>

                <v-btn
                  border
                  color="primary"
                  icon="mdi-circle-edit-outline"
                  height="35"
                  width="35"
                  variant="text"
                  @click="updateTask(item)"/>

              </v-item-group>
            </template>

          </v-data-table-server>
        </v-sheet>
      </v-col>
      <v-col cols="12" sm="1" class="d-flex flex-row flex-sm-column justify-end justify-sm-start">

        <v-btn
          flat
          class="my-3 my-sm-2 mx-3"
          color="primary"
          icon="mdi-checkbox-marked-circle-plus-outline"
          @click="createNewTask"/>

        <v-btn
          flat
          class="my-3 my-sm-1 mx-sm-3"
          color="primary"
          icon="mdi-shape-outline"
          @click="isOverlayActive = !isOverlayActive"/>

        <categories-overlay
          :is-overlay-active="isOverlayActive"
          :context="CategoryType.Task"
          :is-editing="false"
          @closing-overlay="handleClosingOverlay"/>

      </v-col>

      <v-dialog v-model="isTaskEditorActive" max-width="800">
        <template v-slot:default="{ isActive }">
          <v-card rounded="lg">
            <v-card-title class="d-flex justify-space-between align-center">
              <div class="text-h5 text-medium-emphasis ps-2">
                {{ task.id === "" ? "Create Task" : "Edit Task" }}
              </div>

              <v-btn
                icon="mdi-close"
                variant="text"
                @click="isActive.value = false"
              ></v-btn>
            </v-card-title>

            <v-divider class="mb-4"></v-divider>

            <v-card-text>
              <v-form @submit.prevent>
                <v-container class="pa-1">
                  <v-row dense>
                    <v-col cols="12" sm="6">
                      <v-text-field
                        :rules="validator.titleRules"
                        v-model="task.title"
                        color="primary"
                        label="Title"
                        variant="outlined"/>
                    </v-col>

                    <v-col cols="12" sm="3">
                      <v-select
                        :rules="validator.categoryRules"
                        :items="existingCategories"
                        :item-props="selectCategoryProps"
                        v-model="task.category"
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
                                @click="isOverlayActive = true"/>
                            </template>
                          </v-list-item>
                        </template>
                      </v-select>

                      <categories-overlay
                        :is-overlay-active="isNestedOverlayActive"
                        :context="CategoryType.Task"
                        :is-editing="true"
                        @closing-overlay="handleClosingNestedOverlay"/>

                    </v-col>

                    <v-col cols="12" sm="3">
                      <v-select
                        :items="Object.values(TaskStatus).filter(value => typeof value === 'number')"
                        :item-props="selectStatusProps"
                        v-model="task.status"
                        color="primary"
                        label="Status"
                        variant="outlined">

                        <template v-slot:selection="{ item }">
                          <v-chip
                            density="compact"
                            :text="item.title"
                            :color="statusMapper.getColour(item.value)">
                          </v-chip>
                        </template>

                      </v-select>
                    </v-col>

                    <v-col cols="12" sm="8">
                      <v-textarea
                        :rules="validator.contentRules"
                        v-model="task.content"
                        class="ma-0 me-sm-1 me-md-3"
                        color="primary"
                        label="What's the task? 🤔"
                        variant="outlined"
                        rows="2"
                        auto-grow/>
                    </v-col>

                    <v-col cols="12" sm="4" class="d-flex flex-column d-sm-block">
                      <v-btn
                        rounded
                        color="primary"
                        variant="flat">

                        Schedule date

                        <v-dialog
                          width="auto"
                          close-on-back
                          activator="parent">

                          <v-date-picker
                            v-model="task.scheduledAt"
                            :min="new Date()"
                            show-adjacent-months
                            mode-icon="mdi-calendar"
                            rounded="xl"
                            color="primary"/>

                        </v-dialog>
                      </v-btn>

                      <v-container class="px-2 pt-1">
                        <div class="text-caption">
                          Scheduled for
                        </div>
                        <div class="text-body-2">
                          {{
                            new Date((task as TaskData).scheduledAt).toLocaleDateString("en-UK", {
                              weekday: 'long',
                              year: 'numeric',
                              month: 'long',
                              day: 'numeric'
                            })
                          }}
                        </div>
                      </v-container>
                    </v-col>
                  </v-row>
                </v-container>
              </v-form>
            </v-card-text>

            <v-divider class="mt-2"/>

            <v-card-actions class="my-2 mx-2 d-flex justify-end">
              <v-btn
                rounded
                text="Cancel"
                @click="isTaskEditorActive = false"/>

              <v-btn
                rounded
                color="primary"
                text="Submit"
                variant="flat"
                @click="saveTask"/>
            </v-card-actions>
          </v-card>
        </template>
      </v-dialog>
    </v-row>
  </v-container>
</template>

<style scoped>

</style>

<script lang="ts" setup>
import {onBeforeMount, ref, watch} from "vue";
import {TaskData} from "../../models/TaskData";
import CategoriesOverlay from "@/components/CategoriesOverlay.vue";
import {CategoryData} from "../../models/CategoryData";
import {TaskStatus, TaskStatusMapper} from "../../models/enums/TaskStatus";
import {TaskEditorValidator} from "../../models/validators/TaskEditorValidator";
import {CategoryType} from "../../models/enums/CategoryType";
import {GlobalSnackbar} from "@/store/Snackbar";
import {TasksClient} from "../../api/clients/TasksClient";
import {CategoriesClient} from "../../api/clients/CategoriesClient";
import {useAuth0} from "@auth0/auth0-vue";

const snackbar = GlobalSnackbar();
const {getAccessTokenSilently} = useAuth0();
const tasksClient = ref<TasksClient>(new TasksClient(getAccessTokenSilently));
const categoriesClient = ref<CategoriesClient>(new CategoriesClient(getAccessTokenSilently));

const tableState = ref<number>(0);
const itemLength = ref<number>(0);
const searchString = ref<string>("");
const isTableLoading = ref<boolean>(false);

const statusMapper = ref<TaskStatusMapper>(new TaskStatusMapper());
const isTaskEditorActive = ref<boolean>(false);
const isOverlayActive = ref<boolean>(false);
const isNestedOverlayActive = ref<boolean>(false);

const task = ref<TaskData>(new TaskData());
const validator = ref<TaskEditorValidator>(new TaskEditorValidator());

const tableItems = ref<Array<TaskData>>(new Array<TaskData>());
const expandedTasks = ref<Array<TaskData>>(new Array<TaskData>());
const existingCategories = ref<Array<CategoryData>>(new Array<CategoryData>());

const tableHeaders: Array<any> = [
  {title: 'Title', key: 'title', align: 'start', sortable: true},
  {title: 'Content', key: 'content', align: 'start', sortable: false},
  {title: 'Status', key: 'status', align: 'start', sortable: false},
  {title: 'Category', key: 'category', align: 'start', sortable: false},
  {title: 'Modified at', key: 'modifiedAt', align: 'start', sortable: true},
  {title: 'Scheduled at', key: 'scheduledAt', align: 'start', sortable: true},
  {title: 'Actions', key: 'actions', align: 'center', sortable: false},
];

onBeforeMount(async () => {
  await loadCategories();
});

watch(
  () => task.value.scheduledAt,
  (newValue) => {
    // IDE is wrong, update scenarios the value won't be a date
    // therefore we need to ensure the correct type of the attribute
    if (!(newValue instanceof Date)) {
      task.value.scheduledAt = new Date(newValue);
    }
  },
  { immediate: true }
);

async function loadTableData({page, itemsPerPage, sortBy, search}: any) {
  isTableLoading.value = true;

  const notes = (await tasksClient.value.getTasks()).filter(item => {
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

async function loadCategories() {
  existingCategories.value = await categoriesClient.value.getCategoriesByType(CategoryType.Task)
}

function selectCategoryProps(category: CategoryData) {
  return {
    title: category.name,
    value: category
  }
}

function selectStatusProps(status: any) {
  return {
    title: statusMapper.value.getDescription(status),
    value: status
  }
}

async function handleClosingOverlay() {
  isOverlayActive.value = false;
  await loadCategories();
}

async function handleClosingNestedOverlay() {
  isNestedOverlayActive.value = false;
  await loadCategories();
}

function createNewTask() {
  task.value = new TaskData();
  isTaskEditorActive.value = true;
}

function updateTask(selected: TaskData) {
  task.value = selected;
  isTaskEditorActive.value = true;
}

async function saveTask() {
  const isCreation: Boolean = task.value.id == "";
  let success: Boolean;

  if (isCreation) {
    success = await tasksClient.value.createTask(task.value);
  } else {
    success = await tasksClient.value.updateTask(task.value);
  }

  if (success) {
    snackbar.show("mdi-hand-okay", "success", "Task correctly created.");
    isTaskEditorActive.value = false;
    reloadTable();
  } else {
    snackbar.show("mdi-close-box-outline", "error","An error occurred.");
  }
}

async function deleteTask(id: string) {
  const success: Boolean = await tasksClient.value.deleteTask(id);
  if (success) {
    snackbar.show("mdi-hand-okay", "success", "Task correctly deleted.");
    reloadTable();
  } else {
    snackbar.show("mdi-close-box-outline", "error", "An error occurred.");
  }
}

function reloadTable() {
  tableState.value += 1;
}

</script>
