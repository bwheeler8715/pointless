<script setup lang="ts">
import {computed, ref} from "vue";
import {RouterLink, RouterView, useRoute} from 'vue-router'
import {UserPlusIcon} from '@heroicons/vue/24/solid'

import Modal from "@/components/Modal.vue";

const route = useRoute()

const showInviteModal = ref(false)
const inviteLink = computed(() => {
    const roomId = route.params.roomId
    if (!roomId) return ''
    return `${window.location.origin}/join/${roomId}`
})

const copyToClipboard = () => {
    navigator.clipboard.writeText(inviteLink.value)
    alert('Organizer link copied to clipboard!')
}
</script>
<template>
    <div class="flex items-center fixed top-0 left-0 right-0 p-4 h-16 px-4 border-b z-50 bg-[var(--color-background)]">
        <RouterLink to="/" class="px-4 py-2 bg-blue-500 text-white rounded">Home</RouterLink>
        <button v-if="route.meta.inRoom"
                @click="showInviteModal = true"
                class="ml-auto">
            <UserPlusIcon class="h-10 w-10 text-blue-500"/>
        </button>
    </div>

    <div class="mt-24">
        <RouterView/>
    </div>

    <Modal :is-open="showInviteModal"
           title="Invite People"
           @close="showInviteModal = false">
        <div class="grid grid-cols-1 gap-4">
            <h1 class="text-gray-600">Share this link to invite others to the room:</h1>
            <div class="flex gap-2">
                <input v-model="inviteLink"
                       readonly
                       class="flex-1 p-2 rounded text-black outline outline-black/20"
                       type="text"/>
                <button @click="copyToClipboard"
                        class="rounded px-4 py-2 text-white bg-sky-500 hover:bg-sky-700">
                    Copy
                </button>
            </div>
        </div>
    </Modal>
</template>

<style scoped></style>