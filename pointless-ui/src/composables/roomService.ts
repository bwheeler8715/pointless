import {useHttp} from "./http";

export interface Voter {
    id: string
    name: string
    roomId: string
    vote?: string
}

export interface Room {
    id: string
    name: string
    voters: Voter[]
    votingOpen: boolean
    currentTickerNumber: string
    currentTicketSummary: string
    voteCounts: Record<string, number>
    averageVote: number
}

export interface NewRoom {
    roomId: string
    organizerCode: string
}

export function useRoomService() {
    const {httpPost} = useHttp()

    const getRoom = async (roomId: string, roomName: string = ''): Promise<Room> => {
        const response = await httpPost('/room/get', {
            id: roomId,
            name: roomName
        }, {})
        return {
            id: response.data.id,
            name: response.data.name,
            voters: response.data.voters,
            votingOpen: response.data.votingOpen,
            currentTickerNumber: response.data.currentTickerNumber,
            currentTicketSummary: response.data.currentTicketSummary,
            voteCounts: response.data.voteCounts,
            averageVote: response.data.averageVote
        }
    }

    const createRoom = async (roomName: string): Promise<NewRoom> => {
        const response = await httpPost('/room/create', {
            name: roomName
        }, {});
        return {
            roomId: response.data.roomId,
            organizerCode: response.data.organizerCode,
        };
    };

    const joinRoom = async (roomId: string, roomName: string, voterName: string): Promise<Voter> => {
        const response = await httpPost('/room/join', {
            id: roomId,
            name: roomName,
            voter: voterName
        }, {});
        return {
            id: response.data.id,
            name: response.data.name,
            roomId: response.data.roomId
        };
    }

    const leaveRoom = (roomId: string, voterId: string) => {
        httpPost('/room/leave', {
            roomId: roomId,
            id: voterId
        }, {});
    }

    const openVoting = (roomId: string, ticketNumber: string, ticketSummary: string, organizerCode: string) => {
        httpPost('/room/open', {
            roomId: roomId,
            currentTickerNumber: ticketNumber,
            currentTicketSummary: ticketSummary,
            organizerCode: organizerCode
        }, {});
    }

    const closeVoting = (roomId: string, organizerCode: string) => {
        httpPost('/room/close', {
            roomId: roomId,
            organizerCode: organizerCode
        }, {});
    }

    const vote = (roomId: string, voterId: string, vote: string) => {
        httpPost('/room/vote', {
            roomId: roomId,
            voterId: voterId,
            vote: vote
        }, {});
    }

    return {getRoom, createRoom, joinRoom, leaveRoom, openVoting, closeVoting, vote}
}