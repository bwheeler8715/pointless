<script setup lang="ts">
import {ref} from "vue";
import router from "@/router.js";
import {useRoomService} from "@/composables/roomService.ts";

const roomService = useRoomService()

const roomName = ref('')
const roomId = ref('')

const observeRoom = async () => {
    try {
        const room = await roomService.getRoom(roomId.value, roomName.value)
        router.push(`/room/${room.id}`)
    } catch (e) {
        alert('Room not found. Please check with your organizer and try again.')
    }

}
</script>

<template>
    <div class="grid grid-cols-1 gap-[3rem] justify-items-center">
        <h1 class="text-3xl font-bold">Observe a Room</h1>

        <div class="grid grid-cols-1 gap-1 w-full max-w-80">
            <label for="roomName" class="font-medium text-sm">Room Name</label>
            <input id="roomName"
                   v-model="roomName"
                   class="rounded w-full text-black outline outline-black/20 px-3 py-2"
                   type="text"/>
        </div>

        <h1 class="text-2xl font-bold">Or</h1>

        <div class="grid grid-cols-1 gap-1 w-full max-w-80">
            <label for="roomId" class="font-medium text-sm">Room Id</label>
            <input id="roomId"
                   v-model="roomId"
                   class="rounded w-full text-black outline outline-black/20 px-3 py-2"
                   type="text"/>
        </div>

        <button @click="observeRoom"
                :disabled="!roomName && !roomId"
                :class="{ 'opacity-50 cursor-not-allowed': !roomName && !roomId}"
                class="rounded-xl px-5 py-2 font-semibold w-full max-w-80 text-white bg-sky-500 hover:bg-sky-700">
            Observe Room
        </button>
    </div>
</template>

<style scoped></style>