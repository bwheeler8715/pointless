<script setup lang="ts">
import {useRoute} from "vue-router";
import {CheckIcon, QuestionMarkCircleIcon, XCircleIcon} from "@heroicons/vue/24/solid";
import {useRoomService, type Voter} from "@/composables/roomService.ts";

defineProps<{
    voters: Array<Voter>;
    votingOpen: boolean;
}>();

const route = useRoute()
const roomService = useRoomService()

const removeVoter = (voter: Voter) => {
    roomService.leaveRoom(voter.roomId, voter.id)
}
</script>

<template>
    <div class="grid grid-cols-1 gap-[0.5rem]">
        <h1 class="text-2xl font-bold">Voters</h1>
        <div v-for="voter in voters"
             class="grid grid-cols-3 rounded-md p-2 text-white outline outline-black/5 bg-slate-800">
            <span class="col-span-2 pl-3">{{ voter.name }}</span>
            <div class="col-span-1 flex justify-end center-itmes pr-3">
                <span v-if="!voter.vote"><QuestionMarkCircleIcon class="w-7 h-7 text-gray-500"/></span>
                <span v-if="votingOpen && voter.vote"><CheckIcon class="w-7 h-7 text-green-500"/></span>
                <span v-if="!votingOpen && voter.vote" class="h-7 font-semibold text-lg">{{ voter.vote }}</span>
                <span v-if="route.meta.organizer" class="pl-3">
                    <XCircleIcon @click="removeVoter(voter)" class="w-7 h-7 text-red-500"/>
                </span>
            </div>
        </div>
    </div>
</template>

<style scoped></style>