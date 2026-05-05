<script setup lang="ts">
import {ref} from 'vue'
import router from "@/router.js";
import {useRoomService} from '@/composables/roomService'

const roomService = useRoomService()

const roomName = ref('')

const createRoom = async () => {
    try {
        const newRoom = await roomService.createRoom(roomName.value)
        router.push(`/room/${newRoom.roomId}/organizer/${newRoom.organizerCode}`)
    } catch (e) {
        alert('Room name already exists. Please choose a different name.')
    }
}
</script>

<template>
    <div class="grid grid-cols-1 gap-[3rem] justify-items-center">
        <h1 class="text-3xl font-bold">Create a Room</h1>

        <div class="grid grid-col-1 gap-1 w-full max-w-80">
            <label for="roomName" class="font-medium text-sm">Room Name</label>
            <input id="roomName"
                   v-model="roomName"
                   class="rounded w-full text-black outline outline-black/20 px-3 py-2"
                   type="text"/>
        </div>

        <button @click="createRoom"
                :disabled="!roomName"
                :class="{ 'opacity-50 cursor-not-allowed': !roomName}"
                class="rounded-xl px-5 py-2 font-semibold w-full max-w-80 text-white bg-sky-500 hover:bg-sky-700">
            Create Room
        </button>

    </div>
</template>

<style scoped></style>