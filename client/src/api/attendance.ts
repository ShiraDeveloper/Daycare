import { apiClient } from './client'
import type {
  ActivitiesRequest,
  DailyAttendanceDto,
  GuestParticipationRequest,
  MealSelectionRequest,
  PresenceRequest,
} from '../types'

export async function fetchTodayBoard(): Promise<DailyAttendanceDto[]> {
  const { data } = await apiClient.get<DailyAttendanceDto[]>(
    '/api/attendance/today',
  )
  return data
}

export async function markPresent(
  childId: number,
  body: PresenceRequest = {},
): Promise<DailyAttendanceDto> {
  const { data } = await apiClient.post<DailyAttendanceDto>(
    `/api/attendance/${childId}/present`,
    body,
  )
  return data
}

export async function recordBreakfast(
  childId: number,
  body: MealSelectionRequest,
): Promise<DailyAttendanceDto> {
  const { data } = await apiClient.post<DailyAttendanceDto>(
    `/api/attendance/${childId}/breakfast`,
    body,
  )
  return data
}

export async function recordLunch(
  childId: number,
  body: MealSelectionRequest,
): Promise<DailyAttendanceDto> {
  const { data } = await apiClient.post<DailyAttendanceDto>(
    `/api/attendance/${childId}/lunch`,
    body,
  )
  return data
}

export async function recordActivities(
  childId: number,
  body: ActivitiesRequest,
): Promise<DailyAttendanceDto> {
  const { data } = await apiClient.post<DailyAttendanceDto>(
    `/api/attendance/${childId}/activities`,
    body,
  )
  return data
}

export async function recordGuestParticipation(
  childId: number,
  body: GuestParticipationRequest,
): Promise<DailyAttendanceDto> {
  const { data } = await apiClient.post<DailyAttendanceDto>(
    `/api/attendance/${childId}/guest-participation`,
    body,
  )
  return data
}
