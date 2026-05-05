<script setup lang="ts">
defineProps<{
    voteCounts: Record<string, number>;
    average: number;
}>();
</script>

<template>
    <div class="grid grid-cols-1 gap-[0.5rem]">
        <h1 class="text-2xl font-bold">Results</h1>
        <table class="rounded-lg w-full text-left overflow-hidden border-collapse text-black bg-slate-100">
            <thead class="bg-slate-200">
            <tr>
                <th class="p-2 border-b border-slate-300">Points</th>
                <th class="p-2 border-b border-slate-300 text-right">Count</th>
            </tr>
            </thead>
            <tbody>
            <tr v-for="(count, vote) in voteCounts" :key="vote" class="border-b border-slate-200 last:border-0">
                <td class="p-2">{{ vote }}</td>
                <td class="p-2 text-right font-bold">{{ count }} {{ count === 1 ? 'vote' : 'votes' }}</td>
            </tr>
            </tbody>
            <tfoot>
            <tr class="bg-slate-200 font-bold">
                <td class="p-2 text-lg">Average: {{ average.toFixed(2) }}</td>
                <td/>
            </tr>
            <tr class="bg-slate-200 font-bold">
                <td class="p-2 text-lg">Rounded: {{ average.toFixed() }}</td>
                <td/>
            </tr>
            </tfoot>
        </table>
    </div>
</template>

<style scoped></style>