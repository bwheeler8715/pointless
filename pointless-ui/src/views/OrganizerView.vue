<script setup lang="ts">
import {computed, inject, ref} from 'vue'
import {useRoute} from "vue-router";
import {type Room, useRoomService} from "@/composables/roomService.ts";
import {ShareIcon} from "@heroicons/vue/24/solid";
import Modal from "@/components/Modal.vue";

let route = useRoute();
const roomService = useRoomService()
const roomId = computed(() => route.params.roomId as string)
const organizerCode = computed(() => route.params.organizerCode as string)
const room: Room = <Room>inject('room')

const ticketNumberInput = ref('')
const ticketSummaryInput = ref('')

const openVoting = () => {
    if (!ticketNumberInput.value || !ticketSummaryInput.value) {
        return
    }
    roomService.openVoting(roomId.value, ticketNumberInput.value, ticketSummaryInput.value, organizerCode.value)
}

const closeVoting = () => {
    roomService.closeVoting(roomId.value, organizerCode.value)
    ticketNumberInput.value = ''
    ticketSummaryInput.value = ''
}

const showInviteModal = ref(false)
const inviteLink = computed(() => {
    return `${window.location.origin}/room/${roomId.value}/organizer/${organizerCode.value}`
})

const copyToClipboard = () => {
    navigator.clipboard.writeText(inviteLink.value)
    alert('Invite link copied to clipboard!')
}

</script>

<template>
    <div class="grid grid-cols-3 gap-[0.5rem]">
        <div class="col-span-3 flex">
            <h1 class="text-2xl font-bold">Organizer Controls</h1>
            <button @click="showInviteModal = true"
                    class="ml-auto">
                <ShareIcon class="h-10 w-10 text-blue-500"/>
            </button>
        </div>

        <input v-model="ticketNumberInput"
               :disabled="room.votingOpen"
               class="col-span-3 rounded text-black outline outline-black/20"
               type="text"
               placeholder="Ticket #"/>
        <input v-model="ticketSummaryInput"
               :disabled="room.votingOpen"
               class="col-span-3 rounded text-black outline outline-black/20"
               type="text"
               placeholder="Ticket Summary"/>
        <button @click="openVoting"
                :disabled="!ticketNumberInput || !ticketSummaryInput || room.votingOpen"
                :class="{ 'opacity-50 cursor-not-allowed': !ticketNumberInput || !ticketSummaryInput || room.votingOpen }"
                class="col-span-1 rounded-full px-5 py-2 leading-5 min-w-10 text-sm font-semibold text-white bg-sky-500 hover:bg-sky-700">
            Open Voting
        </button>
        <button @click="closeVoting"
                :disabled="!room.votingOpen"
                :class="{ 'opacity-50 cursor-not-allowed': !room.votingOpen }"
                class="col-span-1 rounded-full px-5 py-2 leading-5 min-w-10 text-sm font-semibold text-white bg-sky-500 hover:bg-sky-700">
            Close Voting
        </button>
        <div class="col-span-1"></div>
    </div>

    <Modal :is-open="showInviteModal"
           title="Invite Organizers"
           @close="showInviteModal = false">
        <div class="grid grid-cols-1 gap-4">
            <h1 class="text-gray-600">Share this link to allow others to organize the room:</h1>
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