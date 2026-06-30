import type {
  AttendanceStatus,
  DailyAttendanceDto,
  NextStep,
  WorkflowState,
} from '../types'

export const NEXT_STEP_LABELS: Record<NextStep, string> = {
  MARK_PRESENCE: 'Mark presence',
  RECORD_BREAKFAST: 'Record breakfast',
  RECORD_LUNCH: 'Record lunch',
  RECORD_ACTIVITIES: 'Record activities',
  RECORD_GUEST_PARTICIPATION: 'Guest participation',
  COMPLETE: 'Complete',
  DONE: 'Done for today',
}

export const WORKFLOW_PROGRESS: WorkflowState[] = [
  'ATTENDANCE_PENDING',
  'PRESENT',
  'BREAKFAST_RECORDED',
  'LUNCH_RECORDED',
  'ACTIVITIES_RECORDED',
  'COMPLETED',
]

export function workflowProgressIndex(state: WorkflowState): number {
  const idx = WORKFLOW_PROGRESS.indexOf(state)
  return idx >= 0 ? idx : 0
}

export function statusLabel(status: AttendanceStatus): string {
  switch (status) {
    case 'PRESENT':
      return 'Present'
    case 'ABSENT':
      return 'Absent'
    case 'ARRIVING_LATE':
      return 'Arriving late'
    default:
      return 'Pending'
  }
}

export function statusColor(status: AttendanceStatus): string {
  switch (status) {
    case 'PRESENT':
      return 'border-emerald-200 bg-emerald-50 text-emerald-800'
    case 'ABSENT':
      return 'border-red-200 bg-red-50 text-red-800'
    case 'ARRIVING_LATE':
      return 'border-amber-200 bg-amber-50 text-amber-800'
    default:
      return 'border-slate-200 bg-white text-slate-600'
  }
}

export function childInitials(name: string): string {
  return name
    .split(/\s+/)
    .map((part) => part[0])
    .join('')
    .slice(0, 2)
    .toUpperCase()
}

export function isWorkflowDone(child: DailyAttendanceDto): boolean {
  return child.nextStep === 'DONE' || child.workflowState === 'COMPLETED'
}

export const ACTIVITY_OPTIONS = [
  'ARTS',
  'CRAFTS',
  'MUSIC',
  'SPORT',
  'STORYTIME',
  'OUTDOOR',
  'OTHER',
] as const
