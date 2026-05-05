<script setup lang="ts">
import {computed, ref} from 'vue'
import {useRoute} from "vue-router";
import router from "@/router.js";
import {useRoomService} from "@/composables/roomService.ts";

const roomService = useRoomService()
const route = useRoute()
const roomIdParam = computed(() => route.params.roomId as string)

const name = ref('')
const roomName = ref('')
const roomId = ref('')

const joinRoom = async () => {
    if (!name.value || (!roomName.value && !roomId.value)) {
        return
    }

    try {
        const voter = await roomService.joinRoom(roomId.value, roomName.value, name.value)
        router.push({
            path: `/room/${voter.roomId}/voter`,
            state: {'voter': JSON.stringify(voter)}
        })
    } catch (e) {
        alert('Room not found. Please check with your organizer and try again.')
    }
}

const joinRoomLinked = async () => {
    if (!name.value) {
        return
    }

    const voter = await roomService.joinRoom(roomIdParam.value, '', name.value)
    router.push({
        path: `/room/${voter.roomId}/voter`,
        state: {'voter': JSON.stringify(voter)}
    })
}

const observeRoomLinked = () => {
    router.push(`/room/${roomIdParam.value}`)
}
</script>

<template>
    <div class="grid grid-cols-1 gap-[3rem] justify-items-center">
        <h1 class="text-3xl font-bold">Join a Room</h1>

        <div class="grid grid-cols-1 gap-1 w-full max-w-80">
            <label for="name" class="font-medium text-sm">Your Name</label>
            <input id="name"
                   v-model="name"
                   class="rounded w-full text-black outline outline-black/20 px-3 py-2"
                   type="text"/>
        </div>

        <div v-if="!roomIdParam" class="grid grid-cols-1 gap-1 w-full max-w-80">
            <label for="roomName" class="font-medium text-sm">Room Name</label>
            <input id="roomName"
                   v-model="roomName"
                   class="rounded w-full text-black outline outline-black/20 px-3 py-2"
                   type="text"/>
        </div>

        <h1 v-if="!roomIdParam" class="text-2xl font-bold">Or</h1>

        <div v-if="!roomIdParam" class="grid grid-cols-1 gap-1 w-full max-w-80">
            <label for="roomId" class="font-medium text-sm">Room Id</label>
            <input id="roomId"
                   v-model="roomId"
                   class="rounded w-full text-black outline outline-black/20 px-3 py-2"
                   type="text"/>
        </div>

        <button v-if="!roomIdParam"
                @click="joinRoom"
                :disabled="!name || (!roomName && !roomId)"
                :class="{ 'opacity-50 cursor-not-allowed': !name || (!roomName && !roomId) }"
                class="rounded-xl px-5 py-2 font-semibold w-full max-w-80 text-white bg-sky-500 hover:bg-sky-700">
            Join Room
        </button>

        <button v-if="roomIdParam"
                @click="joinRoomLinked"
                :disabled="!name"
                :class="{ 'opacity-50 cursor-not-allowed': !name }"
                class="rounded-xl px-5 py-2 font-semibold w-full max-w-80 text-white bg-sky-500 hover:bg-sky-700">
            Join Room
        </button>

        <button v-if="roomIdParam"
                @click="observeRoomLinked"
                class="rounded-xl px-5 py-2 font-semibold w-full max-w-80 text-white bg-sky-500 hover:bg-sky-700">
            Observe Instead
        </button>
    </div>
</template>

<style scoped></style>