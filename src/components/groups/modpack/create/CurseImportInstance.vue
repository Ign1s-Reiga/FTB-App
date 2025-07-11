<script lang="ts" setup>
import {prettyByteFormat} from '@/utils';
import {alertController} from '@/core/controllers/alertController';
import {sendMessage} from '@/core/websockets/websocketsApi';
import {gobbleError} from '@/utils/helpers/asyncHelpers';
import {RouterNames} from '@/router';
import CategorySelector from '@/components/groups/modpack/create/CategorySelector.vue';
import { useRouter } from 'vue-router';
import { FontAwesomeIcon } from '@fortawesome/vue-fontawesome';
import { ModalFooter, ModalBody, Modal, UiButton } from '@/components/ui';
import { ref, useTemplateRef } from 'vue';
import { faFileZipper, faTrash, faUpload } from '@fortawesome/free-solid-svg-icons';
import { useAppStore } from '@/store/appStore.ts';

const router = useRouter()
const appStore = useAppStore();

const { open } = defineProps<{
  open?: boolean;
}>()

const emit = defineEmits<{
  (event: 'close'): void;
}>()

const activeFile = ref<any>(null);
const category = ref("Default");

const inputRef = useTemplateRef('fileInputRef');

async function fileAttach(event: any) {
  const file = event.dataTransfer?.files[0] ?? event.target?.files[0] ?? null;
  if (file == null || !file.name.endsWith('.zip')) {
    alertController.warning('Please select a valid .zip file.')
    return;
  }

  const res = await sendMessage('checkCurseZip', {
    path: file.path ?? 'invalid-path-name-to-break-the-java-size-by-default'
  });

  if (!res.success) {
    alertController.error(res.message ?? "We're unable to detect a CurseForge pack in this zip file.")
    return;
  }

  activeFile.value = {
    name: file.name,
    size: file.size,
    path: file.path,
  };
}

async function  installZip() {
  if (!activeFile.value) {
    return;
  }

  await appStore.controllers.install.requestImport(activeFile.value.path, category.value)
  activeFile.value = null;

  await gobbleError(() => {
    router.push({
      name: RouterNames.ROOT_LIBRARY
    })
  })

  emit('close')
}
</script>

<template>
  <modal :open="open" @closed="emit('close')" :external-contents="true" title="Import from CurseForge" sub-title="Import instances from a CurseForge .zip export file">
    <ModalBody>
      <div
        class="drop-area"
        :class="{'has-file': activeFile}"
        @click="inputRef?.click()"
        @dragenter.prevent
        @dragleave.prevent
        @dragover.prevent
        @drop.prevent="fileAttach($event)"
      >
        <FontAwesomeIcon :icon="faUpload" class="mr-2" size="2x" />
        <p>Drag & Drop a file or select a file</p>
        <input type="file" @change="fileAttach($event)" accept="application/zip" hidden ref="fileInputRef" />
      </div>

      <transition name="transition-fade" :duration="250">
        <p v-if="activeFile" class="font-bold mt-4 text-base mb-2">Selected file</p>
      </transition>
      
      <transition name="transition-fade" :duration="250">
        <div class="file flex items-center p-4 pl-6" v-if="activeFile">
          <FontAwesomeIcon :icon="faFileZipper" size="2x" class="mr-6" />
          <div class="text flex-1">
            <div class="name font-bold">{{ activeFile.name }}</div>
            <div class="size opacity-75">
              {{ prettyByteFormat(activeFile.size) }}
            </div>
          </div>
          <div class="delete" @click="activeFile = null">
            <FontAwesomeIcon :icon="faTrash" />
          </div>
        </div>
      </transition>

      <category-selector class="mt-4" label="Import to category" v-model="category" />
    </ModalBody>
    
    <ModalFooter>
      <div class="flex justify-end">
        <UiButton :wider="true" :disabled="!activeFile" type="success" :icon="faUpload" @click="installZip">
          Install
        </UiButton>
      </div>
    </ModalFooter>
  </modal>
</template>

<style lang="scss" scoped>
.drop-area {
  margin-top: 1rem;
  padding: 5rem 2rem;
  display: flex;
  flex-direction: column;
  align-items: center;
  justify-content: center;
  border: 2px dashed rgba(white, 0.2);
  border-radius: 5px;
  
  hr {
    margin: 1rem 0;
  }

  > svg {
    margin-bottom: 1rem;
  }
}

.file {
  background-color: rgba(white, 0.1);
  border-radius: 5px;

  .text {
    .name {
      word-wrap: break-word;
    }
  }
  
  .delete {
    padding: .5rem;
    cursor: pointer;
  }
}
</style>