export type AttendanceStatus =
  | 'UNKNOWN'
  | 'PRESENT'
  | 'ABSENT'
  | 'ARRIVING_LATE'

export type WorkflowState =
  | 'ATTENDANCE_PENDING'
  | 'PRESENT'
  | 'BREAKFAST_RECORDED'
  | 'LUNCH_RECORDED'
  | 'ACTIVITIES_RECORDED'
  | 'COMPLETED'

export type NextStep =
  | 'MARK_PRESENCE'
  | 'RECORD_BREAKFAST'
  | 'RECORD_LUNCH'
  | 'RECORD_ACTIVITIES'
  | 'RECORD_GUEST_PARTICIPATION'
  | 'COMPLETE'
  | 'DONE'

export type MealType = 'BREAKFAST' | 'LUNCH'

export type ActivityType =
  | 'ARTS'
  | 'CRAFTS'
  | 'MUSIC'
  | 'SPORT'
  | 'STORYTIME'
  | 'OUTDOOR'
  | 'OTHER'

export interface DailyActivityDto {
  id?: number
  activityType: ActivityType
  description?: string
  remarks?: string
}

export interface GuestParticipationDto {
  id?: number
  guestPerformerId?: number
  guestPerformerName?: string
  participated: boolean
  remarks?: string
}

export interface DailyAttendanceDto {
  id?: number
  childId: number
  childName: string
  logDate: string
  attendanceStatus: AttendanceStatus
  workflowState: WorkflowState
  nextStep: NextStep
  guestScheduled: boolean
  breakfastId?: number
  breakfastName?: string
  lunchId?: number
  lunchName?: string
  parentConfirmed: boolean
  parentConfirmedAt?: string
  remarks?: string
  activities?: DailyActivityDto[]
  guestParticipations?: GuestParticipationDto[]
}

export interface MealTemplateDto {
  id: number
  name: string
  mealType: MealType
  description?: string
  active: boolean
}

export interface AuthenticateRequest {
  email: string
  pass: string
}

export type UserRole = 'PARENT' | 'NANNIE' | 'MANAGER'

export interface RegisterRequest {
  name: string
  phone: string
  email: string
  password: string
  role: UserRole
}

export interface NannyDto {
  idNanny?: number
  name: string
  phone: string
  email: string
  role: UserRole
}

export interface PresenceRequest {
  remarks?: string
}

export interface MealSelectionRequest {
  mealTemplateId: number
  remarks?: string
}

export interface ActivitiesRequest {
  activities: DailyActivityDto[]
  remarks?: string
}

export interface GuestParticipationRequest {
  participated: boolean
  remarks?: string
}

export interface ApiErrorBody {
  message?: string
  errors?: Record<string, string>
}
