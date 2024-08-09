<template>
  <v-container class="text-center" v-if="noteContent === ''">
    <div class="text-body-1">It seems you have an empty note... 🤔</div>
  </v-container>

  <v-row v-else no-gutters>
    <v-col v-for="(component, index) in editorComponents" :key="index" cols="12">

      <div v-if="component.type === EditorComponentType.Title" class="text-h4">
        {{ component.content }}
      </div>

      <div v-if="component.type === EditorComponentType.Subtitle" class="text-subtitle-1">
        {{ component.content }}
      </div>

      <div v-if="component.type === EditorComponentType.Heading" class="text-h6 mt-2">
        {{ component.content }}
      </div>

      <div v-if="component.type === EditorComponentType.Paragraph" class="text-body-2 my-2">
        {{ component.content }}
      </div>

      <div v-if="component.type === EditorComponentType.Caption" class="text-caption my-2">
        {{ component.content }}
      </div>

      <v-list
        v-if="component.type === EditorComponentType.List"
        density="compact"
        lines="false"
        variant="flat">
        <v-list-item
          class="text-body-2 editor-list-item"
          v-for="(item, i) in component.listItems"
          :key="i"
          :value="item">

          <template v-slot:prepend>
            <v-icon icon="mdi-pencil-circle-outline" size="small"/>
          </template>

          {{ item }}
        </v-list-item>
      </v-list>

    </v-col>
  </v-row>
</template>

<style scoped>

.editor-list-item {
  pointer-events: none;
  cursor: default;
  opacity: 1;
}

.editor-list-item :deep(.v-list-item__prepend .v-list-item__spacer) {
  width: 24px;
}

</style>

<script setup lang="ts">
import {onBeforeMount, ref, watch} from "vue";
import {EditorComponent} from "../../models/EditorComponent";
import {EditorComponentType} from "../../models/enums/EditorComponentType";
import {EditorComponentsBuilder} from "../../models/builders/EditorComponentsBuilder";

const props = defineProps<{
  noteContent: string;
}>();

const editorComponents = ref<Array<EditorComponent>>(new Array<EditorComponent>());
const componentsBuilder = ref<EditorComponentsBuilder>(new EditorComponentsBuilder());

onBeforeMount(() => { BuildRenderedNote(); })

watch(
  () => props.noteContent,
  () => BuildRenderedNote()
)

function BuildRenderedNote():void {
  editorComponents.value.length = 0; // Clear the existing components
  editorComponents.value = componentsBuilder.value.BuildComponents(props.noteContent);
}

</script>
