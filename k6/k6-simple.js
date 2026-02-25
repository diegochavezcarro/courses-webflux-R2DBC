import http from "k6/http";
import { check, sleep } from "k6";

const BASE_URL = __ENV.BASE_URL || "http://localhost:8080";
const LIMIT = __ENV.LIMIT || "20";
const MAX_VUS = Number(__ENV.MAX_VUS || 1000);

export const options = {
  scenarios: {
    ramp: {
      executor: "ramping-vus",
      startVUs: 0,
      stages: [
        { duration: "15s", target: 20 },            // warmup corto
        { duration: "45s", target: MAX_VUS },       // subir fuerte
        { duration: "60s", target: MAX_VUS },       // sostener
        { duration: "15s", target: 0 },             // bajar
      ],
      gracefulRampDown: "10s",
    },
  },
  thresholds: {
    http_req_failed: ["rate<0.02"],     // <2% errores
    http_req_duration: ["p(95)<500"],   // ajustá si querés
  },
};

export default function () {
  const res = http.get(`${BASE_URL}/courses?limit=${LIMIT}`, {
    timeout: "2s",
    headers: { Accept: "application/json" },
  });

  check(res, { "200": (r) => r.status === 200 });

  // sin think time para presionar al máximo
  // (si querés realismo, agregá 5-20ms)
  // sleep(0.01);
}