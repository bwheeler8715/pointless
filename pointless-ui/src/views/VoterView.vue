<script setup lang="ts">
import {inject, onUnmounted} from "vue";
import router from "@/router.ts";
import {type Room, useRoomService, type Voter} from "@/composables/roomService.ts";
import {ArrowRightStartOnRectangleIcon} from "@heroicons/vue/24/solid";

const roomService = useRoomService()
const voter: Voter = JSON.parse(history.state.voter);
const room: Room = <Room>inject('room');

const vote = (value: string) => {
    roomService.vote(voter.roomId, voter.id, value);
}

const leaveRoom = () => {
    roomService.leaveRoom(voter.roomId, voter.id);
    router.push('/')
}

onUnmounted(() => {
    roomService.leaveRoom(voter.roomId, voter.id);
})
</script>

<template>
    <div class="grid grid-cols-1 gap-[0.5rem]">
        <div class="flex">
            <h1 class="text-2xl font-bold">Voting as {{ voter.name }}</h1>
            <button @click="leaveRoom"
                    class="ml-auto">
                <ArrowRightStartOnRectangleIcon class="h-10 w-10 text-blue-500"/>
            </button>
        </div>
        <div class="grid grid-cols-5 gap-[0.5rem]">
            <button v-for="value in Array.of('0','0.5','1','2','3','5','8','13','20','40')"
                    @click="vote(value)"
                    :disabled="!room.votingOpen"
                    :class="{ 'opacity-50 cursor-not-allowed': !room.votingOpen }"
                    class="col-span-1 rounded-xl min-h-[5rem] text-sm font-semibold text-white bg-sky-500 hover:bg-sky-700">
                {{ value }}
            </button>
        </div>
    </div>
</template>

<style scoped></style>