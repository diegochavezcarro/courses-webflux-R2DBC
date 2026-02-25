import http from "k6/http";
import { check, sleep } from "k6";

// Config por env vars (así reusás el mismo script para los 3 servicios)
const BASE_URL = __ENV.BASE_URL || "http://localhost:8080";
const LIMIT = __ENV.LIMIT || "100";
const THINK_MS = __ENV.THINK_MS ? parseInt(__ENV.THINK_MS, 10) : 20; // 0-50ms suele estar bien
const TIMEOUT = __ENV.TIMEOUT || "5s";

export const options = {
  thresholds: {
    http_req_failed: ["rate<0.01"],        // <1% errores
    http_req_duration: ["p(95)<500"],      // ajustá según tu objetivo
  },

  scenarios: {
    // 1) Warmup: no sirve para comparar, solo para "calentar" JIT/DB/pool
    warmup: {
      executor: "constant-vus",
      vus: 10,
      duration: "30s",
      gracefulStop: "5s",
      tags: { scenario: "warmup" },
    },

    // 2) Rampa de concurrencia (VUs)
    ramp_vus: {
      executor: "ramping-vus",
      startVUs: 0,
      stages: [
        { duration: "30s", target: 50 },
        { duration: "60s", target: 100 },
        { duration: "60s", target: 200 },
        { duration: "60s", target: 300 },
        { duration: "60s", target: 300 },
        { duration: "30s", target: 0 },
      ],
      gracefulRampDown: "30s",
      startTime: "35s", // arranca después del warmup
      tags: { scenario: "ramp_vus" },
    },

    // 3) Throughput fijo (arrival rate) para encontrar el límite sin sesgo de VUs
    // Ajustá rate según tu máquina. Empezá bajo y subí.
    arrival_rate: {
      executor: "ramping-arrival-rate",
      startRate: 50,
      timeUnit: "1s",
      preAllocatedVUs: 200,
      maxVUs: 2000,
      stages: [
        { duration: "30s", target: 100 },
        { duration: "60s", target: 200 },
        { duration: "60s", target: 400 },
        { duration: "60s", target: 600 },
        { duration: "60s", target: 600 },
        { duration: "30s", target: 0 },
      ],
      startTime: "6m", // empieza después del escenario ramp_vus
      tags: { scenario: "arrival_rate" },
    },
  },
};

export default function () {
  const url = `${BASE_URL}/courses?limit=${LIMIT}`;
  const res = http.get(url, {
    timeout: TIMEOUT,
    headers: { Accept: "application/json" },
  });

  check(res, {
    "status is 200": (r) => r.status === 200,
    "is JSON": (r) =>
      (r.headers["Content-Type"] || "").includes("application/json"),
    "body not empty": (r) => r.body && r.body.length > 2,
  });

  if (THINK_MS > 0) sleep(THINK_MS / 1000);
}