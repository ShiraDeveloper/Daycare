import type { DailyAttendanceDto } from '../../types'
import {
  childInitials,
  isWorkflowDone,
  NEXT_STEP_LABELS,
  statusColor,
  statusLabel,
  workflowProgressIndex,
  WORKFLOW_PROGRESS,
} from '../../utils/workflow'

interface ChildCardProps {
  child: DailyAttendanceDto
  onClick: () => void
}

export function ChildCard({ child, onClick }: ChildCardProps) {
  const done = isWorkflowDone(child)
  const progress =
    (workflowProgressIndex(child.workflowState) /
      (WORKFLOW_PROGRESS.length - 1)) *
    100

  return (
    <button
      type="button"
      onClick={onClick}
      className={`group flex flex-col rounded-2xl border p-5 text-left shadow-sm transition-all hover:-translate-y-0.5 hover:shadow-md focus:outline-none focus:ring-2 focus:ring-brand-500/30 ${statusColor(child.attendanceStatus)}`}
    >
      <div className="mb-4 flex items-center gap-3">
        <div className="flex h-12 w-12 shrink-0 items-center justify-center rounded-full bg-brand-600 text-sm font-bold text-white shadow-inner">
          {childInitials(child.childName)}
        </div>
        <div className="min-w-0 flex-1">
          <h3 className="truncate text-base font-bold text-slate-900">
            {child.childName}
          </h3>
          <p className="text-xs font-medium opacity-80">
            {statusLabel(child.attendanceStatus)}
          </p>
        </div>
      </div>

      <div className="mb-2 h-1.5 overflow-hidden rounded-full bg-black/5">
        <div
          className="h-full rounded-full bg-brand-600 transition-all duration-500"
          style={{ width: `${done ? 100 : progress}%` }}
        />
      </div>

      <p className="text-xs font-semibold uppercase tracking-wide opacity-70">
        {done
          ? 'All steps complete'
          : NEXT_STEP_LABELS[child.nextStep] ?? child.nextStep}
      </p>

      {child.parentConfirmed && (
        <p className="mt-2 text-xs opacity-60">Parent confirmed</p>
      )}
    </button>
  )
}
