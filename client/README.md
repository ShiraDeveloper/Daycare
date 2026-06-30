# Daycare Educator Client

React + TypeScript frontend for the nursery daily workflow.

## Stack

- **Vite** + **React 19** + **TypeScript**
- **Tailwind CSS v4**
- **Axios** for API calls
- **React Router** for auth routing

## Setup

```bash
cd client
npm install
cp .env.example .env.development   # already present for local dev
npm run dev
```

App runs at `http://localhost:5173`. Backend must be running at `http://localhost:8585` (see `server/`).

## Environment

| Variable | Default | Description |
|----------|---------|-------------|
| `VITE_API_BASE_URL` | `http://localhost:8585` | Spring Boot API base URL |

## Folder structure

```
src/
  api/           Axios client, auth, attendance, meal-templates
  types/         TypeScript DTOs matching the backend
  context/       AuthProvider (JWT in localStorage)
  pages/         Login, Dashboard
  components/    UI, dashboard grid, workflow modal
  utils/         Workflow labels and helpers
```

## Scripts

- `npm run dev` — development server
- `npm run build` — production build
- `npm run preview` — preview production build
